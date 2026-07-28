package com.devstack.POS.dto.response;

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
public class OrderDetailsResponseDTO {
    private UUID productId;
    private String productDescription;
    private Double unitPrice;
    private Integer qty;
}
