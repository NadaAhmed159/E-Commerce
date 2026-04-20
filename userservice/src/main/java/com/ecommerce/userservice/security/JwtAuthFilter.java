package com.ecommerce.userservice.security;

import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.repos.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String tokenHeader = request.getHeader("token");

        if (tokenHeader == null || tokenHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims    = jwtUtil.extractAllClaims(tokenHeader);
            Long userId      = claims.get("id", Long.class);
            String role      = claims.get("role", String.class);
            int tokenVersion = claims.get("tokenVersion", Integer.class);

            User user = userRepository.findById(userId).orElse(null);

            // User not found in DB
            if (user == null) {
                writeErrorResponse(response,
                    "You are not logged in. Please login to get access");
                return;
            }

            // Old token — password was changed after this token was issued
            if (user.getTokenVersion() != tokenVersion) {
                writeErrorResponse(response,
                    "User recently changed password! Please login again.");
                return;
            }

            // Valid token — populate SecurityContext
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ex) {
            writeErrorResponse(response,
                "You are not logged in. Please login to get access");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response,
                                    String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> body = Map.of(
            "statusMsg", "fail",
            "message", message
        );

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}