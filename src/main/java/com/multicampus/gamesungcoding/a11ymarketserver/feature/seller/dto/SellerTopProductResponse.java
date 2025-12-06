package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.SellerTopProduct;

import java.math.BigDecimal;
import java.util.UUID;

public record SellerTopProductResponse(UUID sellerId,
                                       UUID productId,
                                       String productName,
                                       Integer productPrice,
                                       String productImageUrl,
                                       Long orderCount,
                                       Long totalQuantitySold,
                                       BigDecimal totalSalesAmount,
                                       Integer salesRank) {
    public static SellerTopProductResponse fromEntity(SellerTopProduct sellerTopProduct) {
        return new SellerTopProductResponse(
                sellerTopProduct.getId().getSellerId(),
                sellerTopProduct.getId().getProductId(),
                sellerTopProduct.getProductName(),
                sellerTopProduct.getProductPrice().intValue(),
                sellerTopProduct.getProductImageUrl(),
                sellerTopProduct.getOrderCount(),
                sellerTopProduct.getTotalQuantitySold(),
                sellerTopProduct.getTotalSalesAmount(),
                sellerTopProduct.getSalesRank()
        );
    }
}
