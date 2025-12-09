package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductAdminInquireResponse(UUID productId,
                                          String productName,
                                          UUID sellerId,
                                          String sellerName,
                                          SellerGrades sellerGrade,
                                          Boolean isA11yGuarantee,
                                          Integer productPrice,
                                          ProductStatus productStatus,
                                          String productDescription,
                                          Integer productStock,
                                          UUID categoryId,
                                          String categoryName,
                                          LocalDateTime submitDate) {

    public static ProductAdminInquireResponse fromEntity(Product product) {
        return new ProductAdminInquireResponse(
                product.getProductId(),
                product.getProductName(),
                product.getSeller() != null ? product.getSeller().getSellerId() : null,
                product.getSeller() != null ? product.getSeller().getSellerName() : null,
                product.getSeller() != null ? product.getSeller().getSellerGrade() : null,
                product.getSeller() != null ? product.getSeller().getIsA11yGuarantee() : false,
                product.getProductPrice(),
                product.getProductStatus(),
                product.getProductDescription(),
                product.getProductStock(),
                product.getCategory() != null ? product.getCategory().getCategoryId() : null,
                product.getCategory() != null ? product.getCategory().getCategoryName() : null,
                product.getSubmitDate()
        );
    }
}
