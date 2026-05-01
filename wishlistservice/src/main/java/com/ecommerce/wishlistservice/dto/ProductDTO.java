package com.ecommerce.wishlistservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String id;
    private String title;
    private String slug;
    private String description;
    private double price;
    private int quantity;
    private int sold;
    private String imageCover;
    private double ratingsAverage;
    private int ratingsQuantity;
}