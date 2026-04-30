package com.devstack.POS.util;

import com.devstack.POS.dto.request.OrderDetailsRequestDTO;
import com.devstack.POS.entity.Customer;
import com.devstack.POS.entity.CustomerOrder;
import com.devstack.POS.entity.OrderDetails;
import com.devstack.POS.entity.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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

}
