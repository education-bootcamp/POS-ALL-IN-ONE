package com.devstack.POS.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LoginRequestDTO {
    @NotBlank(message = "Email is Required..")
    @Email(message = "Email must be a valid email address...")
    private String email;

    @NotBlank(message = "Password is required..")
    private String password;
}
