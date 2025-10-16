package com.ecommerce.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private String productId;

    private String productName;
    private int quantity;
    private BigDecimal price;

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
