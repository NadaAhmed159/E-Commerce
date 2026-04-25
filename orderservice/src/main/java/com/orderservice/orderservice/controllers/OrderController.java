package com.orderservice.orderservice.controllers;

import com.orderservice.orderservice.dto.CreateOrderRequest;
import com.orderservice.orderservice.dto.OrderResponse;
import com.orderservice.orderservice.security.CurrentUser;
import com.orderservice.orderservice.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestHeader("Authorization") String authorization,
                                                @Valid @RequestBody CreateOrderRequest request) {
        OrderResponse created = orderService.create(CurrentUser.id(), authorization, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<OrderResponse>> myOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(orderService.getMyOrders(CurrentUser.id(), page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> myOrder(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getById(CurrentUser.id(), id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@RequestHeader("Authorization") String authorization,
                                                @PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.cancelMyOrder(CurrentUser.id(), authorization, id));
    }
}

