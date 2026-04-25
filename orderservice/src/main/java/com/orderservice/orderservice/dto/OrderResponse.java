package com.orderservice.orderservice.dto;

import com.orderservice.orderservice.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long userId,
        String productId,
        Integer quantity,
        Double unitPrice,
        Double totalPrice,
        OrderStatus status,
        String shippingAddress,
        LocalDateTime createdAt
) {}

