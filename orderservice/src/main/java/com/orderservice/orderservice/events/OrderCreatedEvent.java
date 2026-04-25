package com.orderservice.orderservice.events;

import com.orderservice.orderservice.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        String productId,
        Integer quantity,
        Double unitPrice,
        Double totalPrice,
        OrderStatus status,
        LocalDateTime createdAt
) {}

