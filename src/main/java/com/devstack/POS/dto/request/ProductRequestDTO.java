package com.devstack.POS.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductRequestDTO {
    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,\\-]+$", message = "Description must contain only letters and spaces.")
    private String description;

    @Positive(message = "Unit price must be a positive value")
    @DecimalMin(value = "0.01", message = "Unit Price must be at least 0.01")
    @DecimalMin(value = "9999999.99", message = "Unit price must not exceed 9,999,999.99")
    private double unitPrice;

    @Min(value = 0, message = "QTY on hand cannot be negative")
    @Max(value = 100000, message = "QTY on Hand must not exceed 100,000")
    private int qtyOnHand;
}
