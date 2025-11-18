package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private String cartItemId;
    private String cartId;
    private String productId;
    private int quantity;

    public static CartDTO fromEntity(CartItems cartItems) {
        return CartDTO.builder()
                .cartItemId(cartItems.getCartItemId().toString())
                .cartId(cartItems.getCartId().toString())
                .productId(cartItems.getProductId().toString())
                .quantity(cartItems.getQuantity())
                .build();
    }
}