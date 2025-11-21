package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminOrderManageController {

    private final AdminOrderService adminOrderService;

    // 관리자 - 전체 주문 조회
    @GetMapping("/v1/admin/orders")
    public ResponseEntity<List<AdminOrderResponse>> inquireAllOrders() {
        List<AdminOrderResponse> orders = adminOrderService.getAllOrders();
        log.info("AdminUserManageController - inquireAllOrders: found {} orders", orders.size());
        return ResponseEntity.ok(orders);
    }

    // 관리자 - 특정 주문 조회 (미구현)
    @GetMapping("/v1/admin/orders/{orderId}")
    public ResponseEntity<String> inquireOrderDetails(@PathVariable String orderId) {
        log.info("AdminUserManageController - inquireOrderDetails");

        // Placeholder for future implementation
        return ResponseEntity.ok("Order details inquiry functionality is under development.");
    }

    // 관리자 - 주문 상태 변경 (미구현)
    @PatchMapping("/v1/admin/orders/{orderId}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        log.info("AdminUserManageController - changeOrderStatus");

        // Placeholder for future implementation
        return ResponseEntity.ok("Change order status functionality is under development.");
    }
}
