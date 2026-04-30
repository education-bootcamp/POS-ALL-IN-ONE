package com.devstack.POS.repo;

import com.devstack.POS.entity.Customer;
import com.devstack.POS.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableJpaRepositories
public interface ProductRepo extends JpaRepository<Product, UUID> {
    @Query(value = "SELECT * FROM product WHERE description LIKE ?1", nativeQuery = true)
    public Page<Product> findAllProducts(String searchText, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM product WHERE name LIKE ?1", nativeQuery = true)
    public long countAllProducts(String searchText);
}
