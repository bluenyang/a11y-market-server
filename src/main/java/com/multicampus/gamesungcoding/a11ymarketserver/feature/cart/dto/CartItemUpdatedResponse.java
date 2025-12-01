package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;

import java.util.UUID;

public record CartItemUpdatedResponse(
        UUID cartItemId,
        UUID cartId,
        UUID productId,
        Integer quantity) {

    public static CartItemUpdatedResponse fromEntity(CartItems cartItems) {

        return new CartItemUpdatedResponse(
                cartItems.getCartItemId(),
                cartItems.getCart().getCartId(),
                cartItems.getProduct().getProductId(),
                cartItems.getQuantity()
        );
    }
}
