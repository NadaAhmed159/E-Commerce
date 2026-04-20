package com.productservice.productservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponseDto {
    private String id;
    private String title;
    private String slug;
    private String description;
    private Integer quantity;
    private Double price;
    private Integer sold;
    private String imageCover;
    private List<String> images;
    private CategoryDto category;
    private BrandDto brand;
    private List<SubcategoryDto> subcategory;
    private Double ratingsAverage;
    private Integer ratingsQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}