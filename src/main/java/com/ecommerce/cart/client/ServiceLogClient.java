package com.ecommerce.cart.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ServiceLogClient {
    private final WebClient webClient;

    public ServiceLogClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8083/api/logs").build();
    }

    public void sendLog(String level, String message, Map<String, Object> metadata) {
        try {
            webClient.post()
                    .bodyValue(Map.of(
                            "service", "cart-service",
                            "level", level,
                            "message", message,
                            "metadata", metadata,
                            "timestamp", LocalDateTime.now().toString()
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();
        } catch (Exception e) {
            System.err.println("Failed to send log to service-log: " + e.getMessage());
        }
    }
}
