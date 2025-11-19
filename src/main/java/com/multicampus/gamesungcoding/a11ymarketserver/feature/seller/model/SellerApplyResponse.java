package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * [SellerApplyResponse]
 * - 판매자 가입 신청 결과 응답 DTO
 * - 서버 → 클라이언트로 반환되는 데이터 전용
 */
public record SellerApplyResponse(
        UUID sellerId,
        String sellerName,
        String businessNumber,
        String sellerGrade,
        String sellerIntro,
        Boolean a11yGuarantee,
        String sellerSubmitStatus,
        LocalDateTime submitDate,
        LocalDateTime approvedDate) {
}
