package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 판매자 - 상품 등록 신청 요청 DTO
 * POST /api/v1/seller/products
 *
 * @param productName        상품명
 * @param productDescription 상품 상세 설명 (선택)
 * @param categoryId         카테고리 ID (FK)
 * @param productPrice       상품 가격
 * @param productStock       상품 재고 수량
 */
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
        productStock) {
}