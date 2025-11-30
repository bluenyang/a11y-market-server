package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ImageMetadata;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SellerProductRegisterRequest(
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
        productStock,

        List<ImageMetadata> imageMetadataList) {
}