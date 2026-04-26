package com.devstack.POS.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsRequestDTO {
    @NotNull(message = "Product id is required.")
    private UUID productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 10000, message = "Quantity must not exceed 10,000")
    private int qty;

    @Positive(message = "Unit price must be a positive value")
    @DecimalMin(value = "0.01", message = "Unit Price must be at least 0.01")
    @DecimalMin(value = "9999999.99", message = "Unit price must not exceed 9,999,999.99")
    private double unitPrice;
}
