package com.devstack.POS.service.impl;

import com.devstack.POS.dto.request.CustomerOrderRequestDTO;
import com.devstack.POS.dto.request.OrderDetailsRequestDTO;
import com.devstack.POS.entity.Customer;
import com.devstack.POS.entity.CustomerOrder;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
}
