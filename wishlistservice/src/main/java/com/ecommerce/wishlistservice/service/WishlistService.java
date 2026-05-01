package com.ecommerce.wishlistservice.service;

import com.ecommerce.wishlistservice.dto.FullWishlistResponse;
import com.ecommerce.wishlistservice.dto.WishlistResponse;

public interface WishlistService {

    WishlistResponse addToWishlist(String productId, String token);

    WishlistResponse removeFromWishlist(String productId, String token);

    FullWishlistResponse getWishlist(String token);
}