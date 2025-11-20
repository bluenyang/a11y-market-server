package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 판매자 - 상품 수정 요청 DTO
 * PUT /api/v1/seller/products/{productId}
 *
 * @param productName        상품명
 * @param productDescription 상품 상세 설명
 * @param categoryId         카테고리 ID (FK)
 * @param productPrice       상품 가격
 * @param productStock       상품 재고 수량
 */
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