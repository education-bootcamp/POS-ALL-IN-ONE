package com.devstack.POS.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerOrderRequestDTO {

    @NotNull(message = "Order date is required..")
    @PastOrPresent(message = "Order date cannot be a future date")
    private LocalDate date;

    @NotNull(message = "Customer id is required.")
    private UUID customerId;

    @NotNull(message = "Order Details cannot be null")
    @NotEmpty(message = "Order must contain at least one product")
    @Size(max = 100, message = "order cannot contain more than 100 products")
    @Valid // ------------------- for each rows
    private List<OrderDetailsRequestDTO> details;
}
