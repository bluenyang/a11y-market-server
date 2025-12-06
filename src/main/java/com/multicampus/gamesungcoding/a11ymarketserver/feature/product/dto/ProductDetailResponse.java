package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductAiSummary;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades;

import java.util.List;
import java.util.UUID;

public record ProductDetailResponse(UUID productId,
                                    String productName,
                                    UUID sellerId,
                                    String sellerName,
                                    SellerGrades sellerGrade,
                                    Boolean isA11yGuarantee,
                                    Integer productPrice,
                                    ProductStatus productStatus,
                                    String productDescription,
                                    Integer productStock,
                                    List<ProductImageResponse> productImages,
                                    UUID categoryId,
                                    String categoryName,
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
            images = java.util.List.of();
        }

        if (summary == null) {
            summary = ProductAiSummary.builder().build();
        }

        var seller = product.getSeller();
        var category = product.getCategory();

        return new ProductDetailResponse(
                product.getProductId(),
                product.getProductName(),
                seller.getSellerId(),
                seller.getSellerName(),
                seller.getSellerGrade(),
                seller.getIsA11yGuarantee(),
                product.getProductPrice(),
                product.getProductStatus(),
                product.getProductDescription(),
                product.getProductStock(),
                images.stream()
                        .map(ProductImageResponse::fromEntity)
                        .toList(),
                category.getCategoryId(),
                category.getCategoryName(),
                summary.getSummaryText(),
                summary.getUsageContext(),
                summary.getUsageMethod()
        );
    }
}
