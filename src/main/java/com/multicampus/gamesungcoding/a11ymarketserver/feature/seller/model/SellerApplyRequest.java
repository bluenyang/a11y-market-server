package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import jakarta.validation.constraints.NotBlank;

/**
 * [SellerApplyRequest]
 * - 판매자 가입 신청 요청 DTO
 * - 클라이언트 → 서버로 들어오는 데이터 전용
 *
 * @param sellerName
 * @param businessNumber
 * @param sellerIntro
 */
public record SellerApplyRequest(
        @NotBlank String sellerName,
        @NotBlank String businessNumber,
        String sellerIntro) {
}