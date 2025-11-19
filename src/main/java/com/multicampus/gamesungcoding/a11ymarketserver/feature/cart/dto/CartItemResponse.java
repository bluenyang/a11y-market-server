package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import java.util.UUID;

public record CartItemResponse(
        UUID cartItemId,
        UUID cartId,
        UUID productId,
        String productName,
        Integer productPrice,
        String productQuantity,
        Integer quantity) {
}