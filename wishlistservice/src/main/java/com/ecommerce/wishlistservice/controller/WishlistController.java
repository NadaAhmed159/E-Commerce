package com.ecommerce.wishlistservice.controller;

import com.ecommerce.wishlistservice.dto.FullWishlistResponse;
import com.ecommerce.wishlistservice.dto.WishlistRequest;
import com.ecommerce.wishlistservice.dto.WishlistResponse;
import com.ecommerce.wishlistservice.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // POST /api/v1/wishlist
    @PostMapping
    public ResponseEntity<WishlistResponse> addToWishlist(
            @Valid @RequestBody WishlistRequest request,
            @RequestHeader("Authorization") String token) {

        WishlistResponse response = wishlistService.addToWishlist(request.getProductId(), token);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/v1/wishlist/{productId}
    @DeleteMapping("/{productId}")
    public ResponseEntity<WishlistResponse> removeFromWishlist(
            @PathVariable String productId,
            @RequestHeader("Authorization") String token) {

        WishlistResponse response = wishlistService.removeFromWishlist(productId, token);
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/wishlist
    @GetMapping
    public ResponseEntity<FullWishlistResponse> getWishlist(
            @RequestHeader("Authorization") String token) {

        FullWishlistResponse response = wishlistService.getWishlist(token);
        return ResponseEntity.ok(response);
    }
}