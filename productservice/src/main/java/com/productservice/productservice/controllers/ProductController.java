package com.productservice.productservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.productservice.productservice.dto.ProductListResponseDto;
import com.productservice.productservice.dto.ProductStockResponse;
import com.productservice.productservice.dto.ReserveStockRequest;
import com.productservice.productservice.entities.Product;
import com.productservice.productservice.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProductListResponseDto> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int limit
    ) {
        return ResponseEntity.ok(productService.getAllProducts(page, limit));
    }

    // Public endpoint to get product details by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> viewProductDetails(@PathVariable("id") String id) {
        return ResponseEntity.ok(productService.getById(id));
    }
    
    // Internal usage by order-service
    @GetMapping("/{id}/stock")
    public ResponseEntity<ProductStockResponse> quantityAndPrice(@PathVariable("id") String id) {
        return ResponseEntity.ok(productService.getQuantityAndPrice(id));
    }
    // Reserve stock (ADMIN) — called from order-service with admin/service token
    @PostMapping("/{id}/reserve")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductStockResponse> reserve(@PathVariable("id") String id,
    @Valid @RequestBody ReserveStockRequest request) {
        return ResponseEntity.ok(productService.reserveStock(id, request.quantity()));
    }
    // Internal usage by order-service to release reserved stock if order is cancelled or payment fails
    @PostMapping("/{id}/release")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductStockResponse> release(@PathVariable("id") String id,
                                                        @Valid @RequestBody ReserveStockRequest request) {
        return ResponseEntity.ok(productService.releaseStock(id, request.quantity()));
    }
}