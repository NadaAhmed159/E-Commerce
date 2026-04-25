package com.orderservice.orderservice.services;

import com.orderservice.orderservice.dto.CreateOrderRequest;
import com.orderservice.orderservice.dto.OrderResponse;
import com.orderservice.orderservice.entity.Order;
import com.orderservice.orderservice.entity.OrderStatus;
import com.orderservice.orderservice.events.OrderCreatedEvent;
import com.orderservice.orderservice.events.OrderStatusChangedEvent;
import com.orderservice.orderservice.exception.BadRequestException;
import com.orderservice.orderservice.exception.NotFoundException;
import com.orderservice.orderservice.clients.ProductServiceApi;
import com.orderservice.orderservice.repos.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductServiceApi productServiceApi;

    @Transactional
    public OrderResponse create(Long currentUserId, String authorizationHeader, CreateOrderRequest request) {
        if (currentUserId == null) {
            throw new BadRequestException("Missing authenticated user");
        }
        if (request.quantity() == null || request.quantity() < 1) {
            throw new BadRequestException("Quantity must be >= 1");
        }
        if (request.productId() == null || request.productId().isBlank()) {
            throw new BadRequestException("ProductId is required");
        }

        // Get authoritative price + ensure stock is reserved
        Map<String, Object> stock = productServiceApi.quantityAndPrice(request.productId());
        Double unitPrice = stock == null ? null : asDouble(stock.get("price"));
        Integer availableQty = stock == null ? null : asInt(stock.get("availableQuantity"));
        if (unitPrice == null) {
            throw new BadRequestException("Invalid product price");
        }
        if (availableQty == null || availableQty < request.quantity()) {
            throw new BadRequestException("Insufficient stock");
        }
        productServiceApi.reserve(authorizationHeader, request.productId(), request.quantity());

        Order order = new Order();
        order.setUserId(currentUserId);
        order.setProductId(request.productId());
        order.setQuantity(request.quantity());
        order.setUnitPrice(unitPrice);
        order.setTotalPrice(unitPrice * request.quantity());
        order.setShippingAddress(request.shippingAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        order = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderCreatedEvent(
                order.getId(),
                order.getUserId(),
                order.getProductId(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt()
        ));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getMyOrders(Long currentUserId, int page, int size) {
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 200));
        return orderRepository.findByUserIdOrderByCreatedAtDesc(currentUserId, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(Long currentUserId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (!order.getUserId().equals(currentUserId)) {
            throw new NotFoundException("Order not found");
        }
        return toResponse(order);
    }

    @Transactional
    public OrderResponse cancelMyOrder(Long currentUserId, String authorizationHeader, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (!order.getUserId().equals(currentUserId)) {
            throw new NotFoundException("Order not found");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return toResponse(order);
        }
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BadRequestException("Confirmed orders cannot be cancelled");
        }
        // Release reserved stock
        productServiceApi.release(authorizationHeader, order.getProductId(), order.getQuantity());
        OrderStatus old = order.getStatus();
        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                order.getId(),
                order.getUserId(),
                old,
                order.getStatus(),
                LocalDateTime.now()
        ));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> listOrders(OrderStatus status, int page, int size) {
        PageRequest pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 500),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        if (status == null) {
            return orderRepository.findAll(pageable).map(this::toResponse);
        }
        return orderRepository.findByStatusOrderByCreatedAtDesc(status, pageable).map(this::toResponse);
    }

    @Transactional
    public OrderResponse adminUpdateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (newStatus == null) {
            throw new BadRequestException("Missing status");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cancelled orders cannot change status");
        }
        OrderStatus old = order.getStatus();
        order.setStatus(newStatus);
        order = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                order.getId(),
                order.getUserId(),
                old,
                order.getStatus(),
                LocalDateTime.now()
        ));
        return toResponse(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getProductId(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getShippingAddress(),
                order.getCreatedAt()
        );
    }

    private static Double asDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return n.doubleValue();
        if (value instanceof String s) {
            try { return Double.parseDouble(s); } catch (NumberFormatException ignored) { return null; }
        }
        return null;
    }

    private static Integer asInt(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return n.intValue();
        if (value instanceof String s) {
            try { return Integer.parseInt(s); } catch (NumberFormatException ignored) { return null; }
        }
        return null;
    }
}

