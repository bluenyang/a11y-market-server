package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.service.AdminOrderService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminOrderManageController {

    private final AdminOrderService adminOrderService;

    // 관리자 - 전체 주문 조회
    @GetMapping("/v1/admin/orders")
    public ResponseEntity<List<AdminOrderResponse>> inquireAllOrders() {
        log.info("AdminUserManageController - inquireAllOrders");

        List<AdminOrderResponse> orders = adminOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // 관리자 - 특정 주문 조회
    @GetMapping("/v1/admin/orders/{orderId}")
    public ResponseEntity<OrderDetailResponse> inquireOrderDetails(@PathVariable UUID orderId) {
        log.info("AdminUserManageController - inquireOrderDetails");

        OrderDetailResponse response = adminOrderService.getOrderDetails(orderId);

        return ResponseEntity.ok(response);
    }

    // 관리자 - 주문 상태 변경 (미구현)
    @PatchMapping("/v1/admin/orders/{orderId}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        log.info("AdminUserManageController - changeOrderStatus");

        // Placeholder for future implementation
        return ResponseEntity.ok("Change order status functionality is under development.");
    }
}
