package com.devstack.POS.service;

import com.devstack.POS.dto.request.LoginRequestDTO;
import com.devstack.POS.dto.request.RegisterRequestDTO;
import com.devstack.POS.dto.response.AuthResponseDTO;

public interface AuthService {
    void register(RegisterRequestDTO dto);
    AuthResponseDTO login(LoginRequestDTO dto);
}
