package com.multicampus.gamesungcoding.a11ymarketserver.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyResponse;

import java.util.UUID;

/**
 * [SellerService]
 * - 판매자 관련 비즈니스 로직 인터페이스
 */
public interface SellerService {

    /**
     * 판매자 가입 신청 처리
     *
     * @param userId  로그인한 사용자 ID (세션에서 가져온 값)
     * @param request 판매자 신청 요청 데이터
     * @return 생성된 판매자 정보 응답 DTO
     */
    SellerApplyResponse applySeller(UUID userId, SellerApplyRequest request);
}
