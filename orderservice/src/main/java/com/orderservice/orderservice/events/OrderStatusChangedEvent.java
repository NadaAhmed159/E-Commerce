package com.orderservice.orderservice.events;

import com.orderservice.orderservice.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusChangedEvent(
        Long orderId,
        Long userId,
        OrderStatus oldStatus,
        OrderStatus newStatus,
        LocalDateTime changedAt
) {}

