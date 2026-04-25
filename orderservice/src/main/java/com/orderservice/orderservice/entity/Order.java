package com.orderservice.orderservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    @Column(length = 50)
    private String productId;
    private Integer quantity;
    private Double totalPrice;
    private Double unitPrice;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private OrderStatus status; // PENDING, CONFIRMED, CANCELLED
    private String shippingAddress;
}
