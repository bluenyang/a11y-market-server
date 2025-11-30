package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductAiSummary;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;

import java.util.List;
import java.util.UUID;

public record ProductDetailResponse(UUID productId,
                                    String productName,
                                    Integer productPrice,
                                    ProductStatus productStatus,
                                    String productDescription,
                                    List<ProductImageResponse> productImages,
                                    String summaryText,
                                    String usageContext,
                                    String usageMethod) {

    public static ProductDetailResponse fromEntity(Product product,
                                                   List<ProductImages> images,
                                                   ProductAiSummary summary) {
        if (product == null) {
            throw new DataNotFoundException("Product cannot be null");
        }

        if (images == null || images.isEmpty()) {
            images = List.of();
        }

        if (summary == null) {
            summary = ProductAiSummary.builder().build();
        }

        return new ProductDetailResponse(
                product.getProductId(),
                product.getProductName(),
                product.getProductPrice(),
                product.getProductStatus(),
                product.getProductDescription(),
                images.stream()
                        .map(ProductImageResponse::fromEntity)
                        .toList(),
                summary.getSummaryText(),
                summary.getUsageContext(),
                summary.getUsageMethod()
        );
    }
}
