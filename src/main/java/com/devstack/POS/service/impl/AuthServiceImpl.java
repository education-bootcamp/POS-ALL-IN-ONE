package com.devstack.POS.service.impl;

import com.devstack.POS.dto.request.LoginRequestDTO;
import com.devstack.POS.dto.request.RegisterRequestDTO;
import com.devstack.POS.dto.response.AuthResponseDTO;
import com.devstack.POS.entity.SystemUser;
import com.devstack.POS.exception.DuplicateEntryException;
import com.devstack.POS.repo.SystemUserRepo;
import com.devstack.POS.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final SystemUserRepo systemUserRepo;

    @Override
    public void register(RegisterRequestDTO dto) {
        if (systemUserRepo.existsByEmail(dto.getEmail())){
            throw new DuplicateEntryException("Email is Already exists");
        }

        SystemUser user = SystemUser.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(dto.getPassword()) //wrong
                .role()
                .isActive()
                .build()
                .build();

    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        return null;
    }
}
