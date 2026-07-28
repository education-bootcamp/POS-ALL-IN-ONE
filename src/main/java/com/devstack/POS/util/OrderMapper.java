package com.devstack.POS.util;

import com.devstack.POS.dto.request.OrderDetailsRequestDTO;
import com.devstack.POS.dto.response.OrderDetailsResponseDTO;
import com.devstack.POS.dto.response.OrderResponseDTO;
import com.devstack.POS.entity.Customer;
import com.devstack.POS.entity.CustomerOrder;
import com.devstack.POS.entity.OrderDetails;
import com.devstack.POS.entity.Product;
import com.devstack.POS.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class OrderMapper {
    public CustomerOrder toCustomerOrder(
            Customer customer, List<OrderDetailsRequestDTO> details, LocalDate date
    ) {
        return CustomerOrder.builder()
                .customer(customer)
                .totalCost(
                        calculate(details)
                ).date(date).build();
    }

    public double calculate(List<OrderDetailsRequestDTO> dtos) {
        double total = 0;
        for (OrderDetailsRequestDTO temp : dtos) {
            double unitPrice = temp.getUnitPrice();
            int qty = temp.getQty();
            total += qty * unitPrice;
        }
        return total;
    }

    public OrderDetails toOrderDetails(CustomerOrder order, Product product, double unitPrice, int qty) {
        return OrderDetails.builder().customerOrder(
                order
        ).product(
                product
        ).qty(
                qty
        ).unitPrice(
                unitPrice
        ).build();
    }

    public OrderDetailsResponseDTO toOrderDetailsResponseDTO(OrderDetails details) {
        if (details == null) throw new ValidationException("Order Details Entity Not Found");
        return OrderDetailsResponseDTO.builder()
                .productId(details.getProduct().getId())
                .productDescription(details.getProduct().getDescription())
                .unitPrice(details.getUnitPrice())
                .qty(details.getQty())
                .build();
    }

    public OrderResponseDTO toOrderResponseDTO(CustomerOrder order) {
        if (order == null) throw new ValidationException("Order Entity Not Found");

        List<OrderDetailsResponseDTO> detailsList = order.getDetailsList() == null
                ? Collections.emptyList()
                : order.getDetailsList().stream()
                        .map(this::toOrderDetailsResponseDTO)
                        .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomer() != null ? order.getCustomer().getId() : null)
                .customerName(order.getCustomer() != null ? order.getCustomer().getName() : null)
                .totalCost(order.getTotalCost())
                .date(order.getDate())
                .details(detailsList)
                .build();
    }
}
