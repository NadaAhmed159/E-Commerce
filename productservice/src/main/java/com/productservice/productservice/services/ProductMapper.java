package com.productservice.productservice.services;

import com.productservice.productservice.dto.*;
import com.productservice.productservice.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponseDto toDto(Product p) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setSlug(p.getSlug());
        dto.setDescription(p.getDescription());
        dto.setQuantity(p.getQuantity());
        dto.setPrice(p.getPrice());
        dto.setSold(p.getSold());
        dto.setImageCover(p.getImageCover());
        dto.setImages(p.getImages());
        dto.setRatingsAverage(p.getRatingsAverage());
        dto.setRatingsQuantity(p.getRatingsQuantity());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());

        CategoryDto cat = new CategoryDto();
        cat.setId(p.getCategoryId());
        cat.setName(p.getCategoryName());
        cat.setSlug(p.getCategorySlug());
        cat.setImage(p.getCategoryImage());
        dto.setCategory(cat);

        BrandDto brand = new BrandDto();
        brand.setId(p.getBrandId());
        brand.setName(p.getBrandName());
        brand.setSlug(p.getBrandSlug());
        brand.setImage(p.getBrandImage());
        dto.setBrand(brand);

        if (p.getSubcategories() != null) {
            List<SubcategoryDto> subs = p.getSubcategories().stream().map(s -> {
                SubcategoryDto sd = new SubcategoryDto();
                sd.setId(s.getId());
                sd.setName(s.getName());
                sd.setSlug(s.getSlug());
                sd.setCategory(s.getCategoryId());
                return sd;
            }).collect(Collectors.toList());
            dto.setSubcategory(subs);
        }

        return dto;
    }
}
