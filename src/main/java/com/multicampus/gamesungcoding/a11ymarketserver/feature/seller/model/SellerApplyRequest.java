package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [SellerApplyRequest]
 * - 판매자 가입 신청 요청 DTO
 * - 클라이언트 → 서버로 들어오는 데이터 전용
 */
@Getter
@NoArgsConstructor
public class SellerApplyRequest {

    /**
     * 상호명
     */
    @NotBlank
    private String sellerName;

    /**
     * 사업자등록번호
     */
    @NotBlank
    private String businessNumber;

    /**
     * 판매자 소개 문구 (선택)
     */
    private String sellerIntro;
}