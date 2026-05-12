package com.devstack.POS.service.impl;

import com.devstack.POS.dto.request.LoginRequestDTO;
import com.devstack.POS.dto.request.RegisterRequestDTO;
import com.devstack.POS.dto.response.AuthResponseDTO;
import com.devstack.POS.entity.ROLE_TYPES;
import com.devstack.POS.entity.SystemUser;
import com.devstack.POS.exception.DuplicateEntryException;
import com.devstack.POS.repo.SystemUserRepo;
import com.devstack.POS.service.AuthService;
import com.devstack.POS.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final SystemUserRepo systemUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequestDTO dto) {
        if (systemUserRepo.existsByEmail(dto.getEmail())){
            throw new DuplicateEntryException("Email is Already exists");
        }

        SystemUser user = SystemUser.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(ROLE_TYPES.USER)
                .isActive(true)
                .build();

        systemUserRepo.save(user);

    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(), dto.getPassword()
                )
        );
        SystemUser systemUser = systemUserRepo.findSystemUserByEmail(dto.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        String token = jwtUtil.generateAccessToken(systemUser);
        return AuthResponseDTO.builder()
                .role(systemUser.getRole().name())
                .token(token)
                .tokenType("Bearer")
                .fullName(systemUser.getFullName())
                .email(systemUser.getEmail())
                .build();
    }
}
