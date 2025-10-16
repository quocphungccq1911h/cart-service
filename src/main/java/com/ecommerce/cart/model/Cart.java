package com.ecommerce.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private List<CartItem> items = new ArrayList<>();

    public Cart(String userId) {
        this.userId = userId;
    }

    public void addItem(CartItem item) {
        if (item == null) return;
        for (CartItem existing : items) {
            if (existing.getProductId().equals(item.getProductId())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        this.items.add(item);
    }

    public void removeItem(String productId) {
        if (productId == null) return;
        items.removeIf(i -> Objects.equals(i.getProductId(), productId));
    }

    public void clear() {
        items.clear();
    }
    public int getTotalQuantity() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
