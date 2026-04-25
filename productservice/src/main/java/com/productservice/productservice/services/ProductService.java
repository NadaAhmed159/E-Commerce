package com.productservice.productservice.services;

// src/main/java/com/ecommerce/product/service/ProductService.java


import com.productservice.productservice.dto.AdminUpsertProductRequest;
import com.productservice.productservice.dto.PageMetaDto;
import com.productservice.productservice.dto.ProductListResponseDto;
import com.productservice.productservice.dto.ProductResponseDto;
import com.productservice.productservice.dto.ProductStockResponse;
import com.productservice.productservice.entities.Product;
import com.productservice.productservice.repos.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductListResponseDto getAllProducts(int page, int limit) {
        // page param from API is 1-based
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponseDto> dtos = productPage.getContent()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        PageMetaDto meta = new PageMetaDto();
        meta.setCurrentPage(page);
        meta.setLimit(limit);
        meta.setNumberOfPages(productPage.getTotalPages());
        meta.setNextPage(page < productPage.getTotalPages() ? page + 1 : null);

        ProductListResponseDto response = new ProductListResponseDto();
        response.setResults((int) productPage.getTotalElements());
        response.setMetadata(meta);
        response.setData(dtos);

        return response;
    }

    @Transactional(readOnly = true)
    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Transactional
    public Product upsertAdmin(AdminUpsertProductRequest request) {
        Product existing = productRepository.findById(request.id()).orElse(null);
        Product product = existing == null ? new Product() : existing;
        product.setId(request.id());
        product.setTitle(request.title());
        product.setSlug(request.slug());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setDescription(request.description());
        product.setImageCover(request.imageCover());
        return productRepository.save(product);
    }

    @Transactional
    public void deleteAdmin(String id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProductStockResponse getQuantityAndPrice(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return new ProductStockResponse(product.getId(), product.getQuantity(), product.getPrice());
    }

    @Transactional
    public ProductStockResponse reserveStock(String productId, int qty) {
        if (qty < 1) throw new IllegalArgumentException("Quantity must be >= 1");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        int available = product.getQuantity() == null ? 0 : product.getQuantity();
        if (available < qty) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        product.setQuantity(available - qty);
        productRepository.save(product);
        return new ProductStockResponse(product.getId(), product.getQuantity(), product.getPrice());
    }

    @Transactional
    public ProductStockResponse releaseStock(String productId, int qty) {
        if (qty < 1) throw new IllegalArgumentException("Quantity must be >= 1");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        int available = product.getQuantity() == null ? 0 : product.getQuantity();
        product.setQuantity(available + qty);
        productRepository.save(product);
        return new ProductStockResponse(product.getId(), product.getQuantity(), product.getPrice());
    }
}
