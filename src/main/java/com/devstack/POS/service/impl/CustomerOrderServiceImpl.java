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

    @Override
    @Transactional
    public void createOrder(CustomerOrderRequestDTO dto) {
        Customer selectedCustomer = customerRepo.findById(dto.getCustomerId()).orElseThrow(() -> new EntryNotFoundException("Customer not found for provided id"));

        CustomerOrder savedData = orderRepo.save(orderMapper.toCustomerOrder(
                selectedCustomer, dto.getDetails(), dto.getDate()
        ));

        for (OrderDetailsRequestDTO temp : dto.getDetails()) {
            Product selectedProduct = productRepo.findById(temp.getProductId())
                    .orElseThrow(() -> new EntryNotFoundException(String.format("Product Not found %s", temp.getProductId())));

            if (temp.getQty() <= selectedProduct.getQtyOnHand()) {
                orderDetailsRepo.save(orderMapper.toOrderDetails(
                        savedData, selectedProduct, temp.getUnitPrice(), temp.getQty()
                ));

                selectedProduct.setQtyOnHand(selectedProduct.getQtyOnHand() - temp.getQty());
                productRepo.save(selectedProduct);

            } else {
                throw new ValidationException("Product qty is mismatch");
            }
        }

    }

    @Override
    @Transactional
    public void updateOrder(CustomerOrderRequestDTO dto, UUID id) {
        CustomerOrder existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Order not found for provided id"));

        Customer selectedCustomer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new EntryNotFoundException("Customer not found for provided id"));

        // restore stock that was reserved by the previous line items
        List<OrderDetails> oldDetails = existingOrder.getDetailsList();
        if (oldDetails != null) {
            for (OrderDetails oldDetail : oldDetails) {
                Product product = oldDetail.getProduct();
                product.setQtyOnHand(product.getQtyOnHand() + oldDetail.getQty());
                productRepo.save(product);
            }
            orderDetailsRepo.deleteAll(oldDetails);
        }

        // validate stock availability for the new line items before committing any change
        for (OrderDetailsRequestDTO temp : dto.getDetails()) {
            Product selectedProduct = productRepo.findById(temp.getProductId())
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
            Product selectedProduct = productRepo.findById(temp.getProductId())
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
                Product product = detail.getProduct();
                product.setQtyOnHand(product.getQtyOnHand() + detail.getQty());
                productRepo.save(product);
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
