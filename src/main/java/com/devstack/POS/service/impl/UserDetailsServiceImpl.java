package com.devstack.POS.service.impl;

import com.devstack.POS.repo.SystemUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SystemUserRepo systemUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return systemUserRepo.findSystemUserByEmail(username)
                .map(user->new User(
                        user.getEmail(),
                        user.getPassword(),
                        user.isActive(),
                        true,
                        true,
                        true,
                        List.of(()->new SimpleGrantedAuthority("ROLE_"+user.getRole().name()))
                ))
                .orElseThrow(()->new UsernameNotFoundException("User not found "+ username));
    }
}
