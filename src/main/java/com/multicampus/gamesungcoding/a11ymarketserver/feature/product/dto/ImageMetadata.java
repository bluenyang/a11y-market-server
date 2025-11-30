package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import jakarta.validation.constraints.NotBlank;

public record ImageMetadata(
        @NotBlank String originalFileName,
        String altText,
        Integer sequence) {
}
