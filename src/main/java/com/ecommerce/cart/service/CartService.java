package com.ecommerce.cart.service;

import com.ecommerce.cart.client.CatalogClient;
import com.ecommerce.cart.client.ServiceLogClient;
import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.CartItem;
import com.ecommerce.cart.model.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ServiceLogClient serviceLogClient;
    private final CatalogClient catalogClient;

    private String getKey(String userId) {
        return "cart:" + userId;
    }

    public Cart getCart(String userId) {
        Object cart = redisTemplate.opsForValue().get(getKey(userId));
        return cart == null ? new Cart() : (Cart) cart;
    }

    public Cart addItem(String userId, CartItem item) {

        // 1. Kiểm tra sản phẩm tồn tại
        ProductDTO product = catalogClient.getProductById(item.getProductId());
        if (product == null) {
            serviceLogClient.sendLog("ERROR", "Product not exists", Map.of("productId", item.getProductId()));
            throw new IllegalArgumentException("Product not found in catalog");
        }

        Cart cart = getCart(userId);
        cart.setUserId(userId);

        cart.addItem(item);

        redisTemplate.opsForValue().set(getKey(userId), cart, 7, TimeUnit.DAYS);

        serviceLogClient.sendLog("INFO", "User added item to cart", Map.of("userId", userId, "productId", item.getProductId(), "quantity", item.getQuantity()));

        return cart;
    }

    public void clearCart(String userId) {

        serviceLogClient.sendLog("INFO", "User removed item from cart",
                Map.of("userId", userId));
        redisTemplate.delete(getKey(userId));
    }
}
