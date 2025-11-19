package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartItemDeleteRequest(
        @NotEmpty(message = "itemIds must not be empty")
        List<@NotNull String> itemIds) {
}
