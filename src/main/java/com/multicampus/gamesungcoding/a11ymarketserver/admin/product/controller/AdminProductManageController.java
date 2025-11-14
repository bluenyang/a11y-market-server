package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminProductManageController {
    // 관리자 - 상품 승인 대기 목록 조회 (미구현)
    @GetMapping("/v1/admin/products/pending")
    public ResponseEntity<String> inquirePendingProducts() {
        log.info("AdminUserManageController - inquirePendingProducts");

        // Placeholder for future implementation
        return ResponseEntity.ok("Pending products inquiry functionality is under development.");
    }

    // 관리자 - 상품 상태 변경 (미구현)
    @PatchMapping("/v1/admin/products/{productId}/status")
    public ResponseEntity<String> changeProductStatus(@PathVariable String productId, @RequestParam String status) {
        log.info("AdminUserManageController - changeProductStatus");

        // Placeholder for future implementation
        return ResponseEntity.ok("Change product status functionality is under development.");
    }

}
