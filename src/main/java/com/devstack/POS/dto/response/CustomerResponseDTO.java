package com.devstack.POS.dto.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerResponseDTO {
    private UUID id;
    private String name;
    private double salary;
    private String address;
}
