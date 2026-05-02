package com.devstack.POS.repo;

import com.devstack.POS.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;
@EnableJpaRepositories
public interface SystemUserRepo extends JpaRepository<SystemUser, UUID> {
    SystemUser findByEmail(String email);
}
