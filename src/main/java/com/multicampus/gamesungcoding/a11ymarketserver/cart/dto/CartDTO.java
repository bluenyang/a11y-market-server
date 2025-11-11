package com.multicampus.gamesungcoding.a11ymarketserver.cart.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.cart.entity.CartItems;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private UUID cartItemId;
    private UUID cartId;
    private UUID productId;
    private int quantity;

    public static CartDTO fromEntity(CartItems cartItems) {
        return CartDTO.builder()
                .cartItemId(cartItems.getCartItemId())
                .cartId(cartItems.getCartId())
                .productId(cartItems.getProductId())
                .quantity(cartItems.getQuantity())
                .build();
    }
}