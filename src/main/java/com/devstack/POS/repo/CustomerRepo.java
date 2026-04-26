package com.devstack.POS.repo;


import com.devstack.POS.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableJpaRepositories
public interface CustomerRepo extends JpaRepository<Customer, UUID> {

}
