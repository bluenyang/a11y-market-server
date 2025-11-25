package com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.service.AdminSellerService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.SellerApplyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminSellerManageController {

    private final AdminSellerService adminSellerService;

    // 관리자 - 판매자 승인 대기 목록 조회
    @GetMapping("/v1/admin/sellers/pending")
    public ResponseEntity<List<SellerApplyResponse>> inquirePendingSellers() {

        List<SellerApplyResponse> pendingSellers = adminSellerService.inquirePendingSellers();

        return ResponseEntity.ok(pendingSellers);
    }

    // 관리자 - 판매자 상태 변경
    @PatchMapping("/v1/admin/sellers/{sellerId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> changeSellerStatus(@PathVariable String sellerId, @RequestParam String status) {

        adminSellerService.updateSellerStatus(UUID.fromString(sellerId), status);
        return ResponseEntity.noContent().build();
    }

    // 관리자 - 판매자 정보 수정 (미구현)
    @PatchMapping("/v1/admin/sellers/{sellerId}")
    public ResponseEntity<String> updateSellerInfo(@PathVariable String sellerId, @RequestBody String sellerInfo) {
        log.info("AdminUserManageController - updateSellerInfo");

        // Placeholder for future implementation
        return ResponseEntity.ok("Update seller information functionality is under development.");
    }
}
