package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 판매자 - 상품 등록 신청 요청 DTO
 * POST /api/v1/seller/products
 */
@Getter
@NoArgsConstructor
public class SellerProductRegisterRequest {
    /**
     * 판매자 ID
     */
    @NotBlank
    private String sellerId;
    /**
     * 상품명
     */
    @NotBlank
    private String productName;

    /**
     * 상품 상세 설명 (선택)
     */
    private String productDescription;

    /**
     * 카테고리 ID (FK)
     */
    @NotBlank
    private String categoryId;

    /**
     * 상품 가격
     */
    @NotNull
    @Min(0)
    private Integer productPrice;

    /**
     * 상품 재고 수량
     */
    @NotNull
    @Min(0)
    private Integer productStock;
}