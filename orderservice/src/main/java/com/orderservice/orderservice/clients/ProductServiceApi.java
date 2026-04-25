package com.orderservice.orderservice.clients;

import com.orderservice.orderservice.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class ProductServiceApi {

    private final RestClient restClient;

    public ProductServiceApi(@Value("${services.product-service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> quantityAndPrice(String productId) {
        Object body = restClient.get()
                .uri("/api/v1/products/{id}/stock", productId)
                .retrieve()
                .body(Object.class);
        if (body instanceof Map<?, ?> m) {
            return (Map<String, Object>) m;
        }
        throw new BadRequestException("Invalid product service response");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> reserve(String authorizationHeader, String productId, int quantity) {
        Object body = restClient.post()
                .uri("/api/v1/products/{id}/reserve", productId)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("quantity", quantity))
                .retrieve()
                .body(Object.class);
        if (body instanceof Map<?, ?> m) {
            return (Map<String, Object>) m;
        }
        throw new BadRequestException("Invalid product service response");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> release(String authorizationHeader, String productId, int quantity) {
        Object body = restClient.post()
                .uri("/api/v1/products/{id}/release", productId)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("quantity", quantity))
                .retrieve()
                .body(Object.class);
        if (body instanceof Map<?, ?> m) {
            return (Map<String, Object>) m;
        }
        throw new BadRequestException("Invalid product service response");
    }
}

