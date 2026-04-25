package com.orderservice.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotNull String productId,
        @NotNull @Min(1) Integer quantity,
        @NotBlank String shippingAddress
) {}

