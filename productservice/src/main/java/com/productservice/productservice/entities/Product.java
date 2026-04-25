package com.productservice.productservice.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer quantity;
    private Double price;
    private Integer sold;

    @Version
    private Long version;

    @Column(name = "image_cover")
    private String imageCover;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

    // Category
    @Column(name = "category_id", length = 50)
    private String categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_slug")
    private String categorySlug;

    @Column(name = "category_image")
    private String categoryImage;

    // Brand
    @Column(name = "brand_id", length = 50)
    private String brandId;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "brand_slug")
    private String brandSlug;

    @Column(name = "brand_image")
    private String brandImage;

    @Column(name = "ratings_average")
    private Double ratingsAverage;

    @Column(name = "ratings_quantity")
    private Integer ratingsQuantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductSubcategory> subcategories;
}