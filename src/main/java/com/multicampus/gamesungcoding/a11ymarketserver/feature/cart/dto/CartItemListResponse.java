package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import jakarta.validation.constraints.Positive;

import java.util.List;

public record CartItemListResponse(
        List<CartItemResponse> items,

        @Positive(message = "Total must be positive")
        int total) {
}