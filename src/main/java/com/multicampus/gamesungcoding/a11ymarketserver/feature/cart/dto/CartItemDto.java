package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import java.util.UUID;

public record CartItemDto(
        UUID cartItemId,
        UUID cartId,
        UUID productId,
        UUID sellerId,
        String sellerName,
        String productName,
        Integer productPrice,
        String categoryName,
        Integer quantity) {
}