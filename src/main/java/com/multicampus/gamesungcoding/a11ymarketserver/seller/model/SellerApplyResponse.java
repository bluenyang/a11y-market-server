package com.multicampus.gamesungcoding.a11ymarketserver.seller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * [SellerApplyResponse]
 * - 판매자 가입 신청 결과 응답 DTO
 * - 서버 → 클라이언트로 반환되는 데이터 전용
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerApplyResponse {

    private UUID sellerId;
    private String sellerName;
    private String businessNumber;
    private String sellerGrade;
    private String sellerIntro;
    private Boolean a11yGuarantee;
    private String sellerSubmitStatus;
    private LocalDateTime submitDate;
    private LocalDateTime approvedDate;
}
