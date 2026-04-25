package com.orderservice.orderservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.orderservice.orderservice.exception.UnauthorizedException;

public final class CurrentUser {
    private CurrentUser() {}

    public static Long id() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        if (auth.getPrincipal() instanceof Long l) {
            return l;
        }
        if (auth.getPrincipal() instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        
        return null;
    }
}

