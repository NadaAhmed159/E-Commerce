package com.productservice.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubcategoryDto {
    @JsonProperty("_id")   // ← add this
    private String id; 
    private String name;
    private String slug;
    private String category;
}
