package com.devstack.POS.repo;

import com.devstack.POS.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface ProductRepo extends JpaRepository<Product, UUID> {
    @Query(value = "SELECT * FROM product WHERE description LIKE ?1", nativeQuery = true)
    public Page<Product> findAllProducts(String searchText, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM product WHERE description LIKE ?1", nativeQuery = true)
    public long countAllProducts(String searchText);

    /**
     * Locks the product row for the duration of the current transaction (SELECT ... FOR UPDATE).
     * Must be used when reading a product's qtyOnHand right before deducting/restoring stock
     * inside placeOrder / updateOrder / deleteOrder, so that two concurrent orders for the
     * same product cannot both pass the stock check and oversell it.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = ?1")
    Optional<Product> findByIdForUpdate(UUID id);
}
