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
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String address;

    private double salary;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private List<CustomerOrder> orders;
}
