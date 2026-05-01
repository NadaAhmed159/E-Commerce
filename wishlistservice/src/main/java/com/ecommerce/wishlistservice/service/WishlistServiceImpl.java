package com.ecommerce.wishlistservice.service;

import com.ecommerce.wishlistservice.dto.FullWishlistResponse;
import com.ecommerce.wishlistservice.dto.ProductDTO;
import com.ecommerce.wishlistservice.dto.WishlistResponse;
import com.ecommerce.wishlistservice.entity.WishlistItem;
import com.ecommerce.wishlistservice.exception.ResourceNotFoundException;
import com.ecommerce.wishlistservice.repo.WishlistRepository;
import com.ecommerce.wishlistservice.util.JwtUtil;
import com.ecommerce.wishlistservice.clients.ProductClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final JwtUtil jwtUtil;
    private final ProductClient productClient;

    @Override
    public WishlistResponse addToWishlist(String productId, String token) {
        String userId = jwtUtil.extractUserId(token);

        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new RuntimeException("Product already in wishlist");
        }

        WishlistItem item = new WishlistItem();
        item.setUserId(userId);
        item.setProductId(productId);
        wishlistRepository.save(item);

        List<String> productIds = wishlistRepository.findAllByUserId(userId)
                .stream()
                .map(WishlistItem::getProductId)
                .collect(Collectors.toList());

        return new WishlistResponse("success", "Product added successfully to your wishlist", productIds);
    }

    @Override
    public WishlistResponse removeFromWishlist(String productId, String token) {
        String userId = jwtUtil.extractUserId(token);

        WishlistItem item = wishlistRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in wishlist"));

        wishlistRepository.delete(item);

        List<String> remaining = wishlistRepository.findAllByUserId(userId)
                .stream()
                .map(WishlistItem::getProductId)
                .collect(Collectors.toList());

        return new WishlistResponse("success", "Product removed successfully from your wishlist", remaining);
    }

    @Override
    public FullWishlistResponse getWishlist(String token) {
        String userId = jwtUtil.extractUserId(token);

        List<ProductDTO> products = wishlistRepository.findAllByUserId(userId)
                .stream()
                .map(item -> productClient.getProductById(item.getProductId()))
                .collect(Collectors.toList());

        return new FullWishlistResponse("success", products.size(), products);
    }
}