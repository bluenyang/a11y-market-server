package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartQtyUpdateRequest(
        @NotNull(message = "Quantity is required.")
        @Min(message = "Quantity must be at least 1.", value = 1)
        int quantity) {
}