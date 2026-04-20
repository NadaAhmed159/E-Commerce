package com.productservice.productservice.services;

// src/main/java/com/ecommerce/product/service/ProductService.java


import com.productservice.productservice.dto.*;
import com.productservice.productservice.entities.Product;
import com.productservice.productservice.repos.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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
}
