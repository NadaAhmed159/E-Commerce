package com.productservice.productservice.entities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_subcategories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSubcategory {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "category_id", length = 50)
    private String categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Product product;
}