package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;

import java.util.List;
import java.util.UUID;

public record ProductResponse(UUID productId,
                              String productName,
                              String productDescription,
                              String sellerName,
                              Boolean isA11yGuarantee,
                              Integer productPrice,
                              List<ProductImageResponse> productImages,
                              UUID categoryId,
                              String categoryName) {

    public static ProductResponse fromEntity(Product product) {

        var seller = product.getSeller();
        var category = product.getCategory();
        return new ProductResponse(
                product.getProductId(),
                product.getProductName(),
                product.getProductDescription(),
                seller.getSellerName(),
                seller.getIsA11yGuarantee(),
                product.getProductPrice(),
                product.getProductImages()
                        .stream()
                        .map(ProductImageResponse::fromEntity)
                        .toList(),
                category.getCategoryId(),
                category.getCategoryName()
        );
    }
}
