package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SellerProductUpdateRequest(
        @NotBlank
        String productName,

        @NotBlank
        String productDescription,

        @NotBlank
        String categoryId,

        @NotNull
        @Min(0) Integer
        productPrice,

        @NotNull
        @Min(0) Integer
        productStock) {
}