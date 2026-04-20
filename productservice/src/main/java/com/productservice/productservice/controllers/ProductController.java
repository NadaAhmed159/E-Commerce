package com.productservice.productservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.productservice.productservice.dto.ProductListResponseDto;
import com.productservice.productservice.services.ProductService;

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
}