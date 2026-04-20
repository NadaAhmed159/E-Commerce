package com.productservice.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CategoryDto {
     @JsonProperty("_id")   // ← add this
    private String id; 
    private String name;
    private String slug;
    private String image;
}
