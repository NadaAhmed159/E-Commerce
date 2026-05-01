package com.productservice.productservice.services;

// import com.productservice.productservice.dto.AdminUpsertProductRequest;
// import com.productservice.productservice.entities.Product;
// import com.productservice.productservice.exception.ResourceNotFoundException;
// import com.productservice.productservice.repos.BrandRepository;
// import com.productservice.productservice.repos.CategoryRepository;
// import com.productservice.productservice.repos.ProductRepository;
// import com.productservice.productservice.repos.SubcategoryRepository;
// import com.productservice.productservice.util.IdValidator;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
public class ProductAdminService {

//     private final ProductRepository   productRepository;
//     private final CategoryRepository  categoryRepository;
//     private final BrandRepository     brandRepository;
//     private final SubcategoryRepository subcategoryRepository;

//     public Product create(AdminUpsertProductRequest request) {
//         Product product = buildProduct(new Product(), request);
//         return productRepository.save(product);
//     }

//     public Product update(String id, AdminUpsertProductRequest request) {
//         IdValidator.validate(id, "id");
//         Product existing = productRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
//         buildProduct(existing, request);
//         return productRepository.save(existing);
//     }

//     public void delete(String id) {
//         IdValidator.validate(id, "id");
//         if (!productRepository.existsById(id)) {
//             throw new ResourceNotFoundException("Product not found with id: " + id);
//         }
//         productRepository.deleteById(id);
//     }

//     // ── private helpers ──────────────────────────────────────────────────────

//     private Product buildProduct(Product product, AdminUpsertProductRequest req) {
//         product.setTitle(req.getTitle());
//         product.setSlug(slugify(req.getTitle()));
//         product.setDescription(req.getDescription());
//         product.setQuantity(req.getQuantity());
//         product.setPrice(req.getPrice());
//         product.setImageCover(req.getImageCover());
//         product.setImages(req.getImages());

//         product.setCategory(
//             categoryRepository.findById(req.getCategoryId())
//                 .map(c -> new Product.CategoryRef(c.getId(), c.getName(), c.getSlug(), c.getImage()))
//                 .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + req.getCategoryId()))
//         );

//         if (req.getBrandId() != null && !req.getBrandId().isBlank()) {
//             product.setBrand(
//                 brandRepository.findById(req.getBrandId())
//                     .map(b -> new Product.BrandRef(b.getId(), b.getName(), b.getSlug(), b.getImage()))
//                     .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + req.getBrandId()))
//             );
//         }

//         if (req.getSubcategoryIds() != null && !req.getSubcategoryIds().isEmpty()) {
//             List<Product.SubcategoryRef> subcats = req.getSubcategoryIds().stream()
//                 .map(subId -> subcategoryRepository.findById(subId)
//                     .map(s -> new Product.SubcategoryRef(s.getId(), s.getName(), s.getSlug(), s.getCategory()))
//                     .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found with id: " + subId))
//                 )
//                 .collect(Collectors.toList());
//             product.setSubcategory(subcats);
//         }

//         return product;
//     }

//     private String slugify(String title) {
//         return title.toLowerCase()
//                     .trim()
//                     .replaceAll("[^a-z0-9\\s-]", "")
//                     .replaceAll("\\s+", "-");
//     }
}