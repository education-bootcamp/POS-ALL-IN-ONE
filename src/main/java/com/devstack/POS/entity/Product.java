package com.devstack.POS.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="product_id")
    private UUID id;

    private String description;

    @Column(name="unit_price")
    private Double unitPrice;

    @Column(name="qty_on_hand")
    private Integer qtyOnHand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private List<OrderDetails> detailsList;

}
