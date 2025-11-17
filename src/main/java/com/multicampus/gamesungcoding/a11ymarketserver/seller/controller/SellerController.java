package com.multicampus.gamesungcoding.a11ymarketserver.seller.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * [SellerController]
 * - 판매자 관련 API 엔드포인트
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SellerController {

    private final SellerService sellerService;

    /**
     * 판매자 가입 신청
     * - POST /api/v1/seller/apply
     * - 세션에서 userId 가져와 사용
     */
    @PostMapping("/v1/seller/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SellerApplyResponse> applySeller(
            //HttpSession session,
            @RequestParam String userIdString,
            @RequestBody @Valid SellerApplyRequest request
    ) {
        // 로그인 시 "userId"를 String(UUID.toString()) 형태로 세션에 저장했다고 가정
        // String userIdString = (String) session.getAttribute("userId");

        if (userIdString == null) {
            // TODO: 공통 예외/에러 응답 구조 적용 예정
            //throw new IllegalStateException("로그인 정보가 없습니다. userId 세션을 확인하세요.");
            return ResponseEntity.notFound().build();
        }

        UUID userId = UUID.fromString(userIdString);

        return ResponseEntity.ok(sellerService.applySeller(userId, request));
    }
}
