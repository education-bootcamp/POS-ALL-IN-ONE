package com.devstack.POS.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerRequestDTO {
    // you *must* find out all the regex patterns.......
    @NotBlank(message = "Name is Required")
    @Size(min = 3, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "name must contain only letters and spaces.")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @Positive(message = "Salary must be a positive value")
    @DecimalMin(value = "0.01", message = "Salary must be at least 0.01")
    @DecimalMin(value = "9999999.99", message = "Salary must be at least 9,999,999.99")
    private double salary;
}
