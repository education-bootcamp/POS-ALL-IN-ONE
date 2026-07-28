package com.devstack.POS.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponseDTO {
    private UUID orderId;
    private UUID customerId;
    private String customerName;
    private double totalCost;
    private LocalDate date;
    private List<OrderDetailsResponseDTO> details;
}
