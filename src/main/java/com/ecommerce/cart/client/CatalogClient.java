package com.ecommerce.cart.client;

import com.ecommerce.cart.model.ProductDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class CatalogClient {
    private final WebClient webClient;

    public CatalogClient(WebClient.Builder builder) {
        // Base URL trỏ sang catalog-service
        this.webClient = builder.baseUrl("http://localhost:8080/api/products").build();
    }

    public ProductDTO getProductById(String productId) {
        try {
            return webClient.get()
                    .uri("/{id}", productId)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();// blocking vì CartService vẫn chạy sync
        } catch (WebClientResponseException.NotFound e) {
            return null;// Không tìm thấy product
        }
    }
}
