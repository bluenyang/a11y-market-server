package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages;

public record ProductImageResponse(String imageUrl, String altText, Integer imageSequence) {
    public static ProductImageResponse fromEntity(ProductImages image) {
        return new ProductImageResponse(
                image.getImageUrl(),
                image.getAltText(),
                image.getImageSequence()
        );
    }
}
