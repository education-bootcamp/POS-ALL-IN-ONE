package com.devstack.POS.service.impl;

import com.devstack.POS.dto.request.CustomerOrderRequestDTO;
import com.devstack.POS.dto.request.OrderDetailsRequestDTO;
import com.devstack.POS.dto.response.OrderResponseDTO;
import com.devstack.POS.dto.response.PagedResponseDTO;
import com.devstack.POS.entity.Customer;
import com.devstack.POS.entity.CustomerOrder;
import com.devstack.POS.entity.OrderDetails;
import com.devstack.POS.entity.Product;
import com.devstack.POS.exception.EntryNotFoundException;
import com.devstack.POS.exception.ValidationException;
import com.devstack.POS.repo.CustomerRepo;
import com.devstack.POS.repo.OrderDetailsRepo;
import com.devstack.POS.repo.OrderRepo;
import com.devstack.POS.repo.ProductRepo;
import com.devstack.POS.service.CustomerOrderService;
import com.devstack.POS.util.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;
    private final OrderMapper orderMapper;
    private final ProductRepo productRepo;
    private final OrderDetailsRepo orderDetailsRepo;

    /**
     * Places a new order.
     * <p>
     * Runs inside a single DB transaction: the order header, every order line,
     * and every product stock deduction are all committed together, or none of
     * them are (e.g. if a product is out of stock, everything already written
     * in this method call is rolled back).
     * <p>
     * Each product row is read with {@code findByIdForUpdate}, which takes a
     * pessimistic write lock (SELECT ... FOR UPDATE). This is what actually
     * protects qtyOnHand: without it, two customers placing an order for the
     * last unit of the same product at the same time could both read
     * "1 in stock", both pass the check, and both get their order confirmed
     * -- overselling the product. With the lock, the second transaction waits
     * for the first to commit (or roll back) before it can read the row, so it
     * always sees the up-to-date stock.
     */
    @Override
    @Transactional
    public void createOrder(CustomerOrderRequestDTO dto) {
        Customer selectedCustomer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new EntryNotFoundException("Customer not found for provided id"));

        CustomerOrder savedOrder = orderRepo.save(orderMapper.toCustomerOrder(
                selectedCustomer, dto.getDetails(), dto.getDate()
        ));

        for (OrderDetailsRequestDTO item : dto.getDetails()) {
            Product lockedProduct = productRepo.findByIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new EntryNotFoundException(
                            String.format("Product not found for id %s", item.getProductId())));

            if (item.getQty() > lockedProduct.getQtyOnHand()) {
                throw new ValidationException(
                        String.format("Insufficient stock for '%s'. Available: %d, Requested: %d",
                                lockedProduct.getDescription(), lockedProduct.getQtyOnHand(), item.getQty()));
            }

            orderDetailsRepo.save(orderMapper.toOrderDetails(
                    savedOrder, lockedProduct, item.getUnitPrice(), item.getQty()
            ));

            lockedProduct.setQtyOnHand(lockedProduct.getQtyOnHand() - item.getQty());
            productRepo.save(lockedProduct);
        }
    }

    @Override
    @Transactional
    public void updateOrder(CustomerOrderRequestDTO dto, UUID id) {
        CustomerOrder existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Order not found for provided id"));

        Customer selectedCustomer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new EntryNotFoundException("Customer not found for provided id"));

        // restore stock that was reserved by the previous line items (locked row-by-row)
        List<OrderDetails> oldDetails = existingOrder.getDetailsList();
        if (oldDetails != null) {
            for (OrderDetails oldDetail : oldDetails) {
                Product lockedProduct = productRepo.findByIdForUpdate(oldDetail.getProduct().getId())
                        .orElseThrow(() -> new EntryNotFoundException("Product not found for provided id"));
                lockedProduct.setQtyOnHand(lockedProduct.getQtyOnHand() + oldDetail.getQty());
                productRepo.save(lockedProduct);
            }
            orderDetailsRepo.deleteAll(oldDetails);
        }

        // validate stock availability for the new line items before committing any change
        for (OrderDetailsRequestDTO temp : dto.getDetails()) {
            Product selectedProduct = productRepo.findByIdForUpdate(temp.getProductId())
                    .orElseThrow(() -> new EntryNotFoundException(String.format("Product Not found %s", temp.getProductId())));

            if (temp.getQty() > selectedProduct.getQtyOnHand()) {
                throw new ValidationException("Product qty is mismatch");
            }
        }

        existingOrder.setCustomer(selectedCustomer);
        existingOrder.setDate(dto.getDate());
        existingOrder.setTotalCost(orderMapper.calculate(dto.getDetails()));
        orderRepo.save(existingOrder);

        for (OrderDetailsRequestDTO temp : dto.getDetails()) {
            Product selectedProduct = productRepo.findByIdForUpdate(temp.getProductId())
                    .orElseThrow(() -> new EntryNotFoundException(String.format("Product Not found %s", temp.getProductId())));

            orderDetailsRepo.save(orderMapper.toOrderDetails(
                    existingOrder, selectedProduct, temp.getUnitPrice(), temp.getQty()
            ));

            selectedProduct.setQtyOnHand(selectedProduct.getQtyOnHand() - temp.getQty());
            productRepo.save(selectedProduct);
        }
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        CustomerOrder existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Order not found for provided id"));

        List<OrderDetails> details = existingOrder.getDetailsList();
        if (details != null) {
            for (OrderDetails detail : details) {
                Product lockedProduct = productRepo.findByIdForUpdate(detail.getProduct().getId())
                        .orElseThrow(() -> new EntryNotFoundException("Product not found for provided id"));
                lockedProduct.setQtyOnHand(lockedProduct.getQtyOnHand() + detail.getQty());
                productRepo.save(lockedProduct);
            }
        }

        orderRepo.deleteById(id);
    }

    @Override
    public OrderResponseDTO findOrderById(UUID id) {
        CustomerOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Order not found for provided id"));
        return orderMapper.toOrderResponseDTO(order);
    }

    @Override
    public PagedResponseDTO<OrderResponseDTO> searchOrders(String searchText, int page, int size) {
        String text = "%" + searchText + "%";
        return PagedResponseDTO.<OrderResponseDTO>builder()
                .dataList(
                        orderRepo.findAllOrders(text, PageRequest.of(page, size))
                                .stream().map(orderMapper::toOrderResponseDTO).toList()
                )
                .dataCount(
                        orderRepo.countAllOrders(text)
                ).build();
    }
}
