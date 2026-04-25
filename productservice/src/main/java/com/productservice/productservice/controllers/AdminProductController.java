package com.productservice.productservice.controllers;

import com.productservice.productservice.dto.AdminUpsertProductRequest;
import com.productservice.productservice.entities.Product;
import com.productservice.productservice.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> upsert(@Valid @RequestBody AdminUpsertProductRequest request) {
        Product saved = productService.upsertAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        productService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}

