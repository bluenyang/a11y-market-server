package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartAddRequest(
        @NotBlank(message = "product id is required")
        String productId,

        @NotNull(message = "quantity is required")
        @Min(value = 1, message = "quantity must be at least 1")
        Integer quantity) {
}