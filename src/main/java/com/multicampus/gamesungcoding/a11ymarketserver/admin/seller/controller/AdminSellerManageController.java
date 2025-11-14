package com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminSellerManageController {

    // 관리자 - 판매자 승인 대기 목록 조회 (미구현)
    @GetMapping("/v1/admin/sellers/pending")
    public ResponseEntity<String> inquirePendingSellers() {
        log.info("AdminUserManageController - inquirePendingSellers");

        // Placeholder for future implementation
        return ResponseEntity.ok("Pending sellers inquiry functionality is under development.");
    }

    // 관리자 - 판매자 상태 변경 (미구현)
    @PatchMapping("/v1/admin/sellers/{sellerId}/status")
    public ResponseEntity<String> changeSellerStatus(@PathVariable String sellerId, @RequestParam String status) {
        log.info("AdminUserManageController - changeSellerStatus");

        // Placeholder for future implementation
        return ResponseEntity.ok("Change seller status functionality is under development.");
    }

    // 관리자 - 판매자 정보 수정 (미구현)
    @PatchMapping("/v1/admin/sellers/{sellerId}")
    public ResponseEntity<String> updateSellerInfo(@PathVariable String sellerId, @RequestBody String sellerInfo) {
        log.info("AdminUserManageController - updateSellerInfo");

        // Placeholder for future implementation
        return ResponseEntity.ok("Update seller information functionality is under development.");
    }
}
