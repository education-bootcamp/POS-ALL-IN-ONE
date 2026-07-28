package com.devstack.POS.repo;

import com.devstack.POS.entity.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableJpaRepositories
public interface OrderRepo extends JpaRepository<CustomerOrder, UUID> {

    @Query(value = "SELECT co.* FROM customer_order co " +
            "JOIN customer c ON co.customer_id = c.id " +
            "WHERE c.name LIKE ?1 OR CAST(co.order_id AS CHAR) LIKE ?1", nativeQuery = true)
    Page<CustomerOrder> findAllOrders(String searchText, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM customer_order co " +
            "JOIN customer c ON co.customer_id = c.id " +
            "WHERE c.name LIKE ?1 OR CAST(co.order_id AS CHAR) LIKE ?1", nativeQuery = true)
    long countAllOrders(String searchText);
}
