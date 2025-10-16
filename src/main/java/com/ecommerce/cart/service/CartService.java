package com.ecommerce.cart.service;

import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String getKey(String userId) {
        return "cart:" + userId;
    }

    public Cart getCart(String userId) {
        Object cart = redisTemplate.opsForValue().get(getKey(userId));
        return cart == null ? new Cart() : (Cart) cart;
    }

    public Cart addItem(String userId, CartItem item) {
        Cart cart = getCart(userId);
        cart.setUserId(userId);

        cart.addItem(item);

        redisTemplate.opsForValue().set(getKey(userId), cart, 7, TimeUnit.DAYS);
        return cart;
    }

    public void clearCart(String userId) {
        redisTemplate.delete(getKey(userId));
    }
}
