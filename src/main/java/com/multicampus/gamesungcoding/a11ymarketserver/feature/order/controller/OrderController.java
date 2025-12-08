package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class OrderController {

    private final OrderService orderService;

    // 결제 준비 (결제 정보 조회)
    @PostMapping("/v1/orders/pre-check")
    public ResponseEntity<OrderCheckoutResponse> preCheck(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderCheckRequest req) {

        return ResponseEntity
                .ok(orderService.getCheckoutInfo(userDetails.getUsername(), req));
    }

    // 주문 생성
    @PostMapping("/v1/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderCreateRequest req) {

        var orderResp = orderService.createOrder(userDetails.getUsername(), req);
        return ResponseEntity
                .created(URI.create("/api/v1/users/me/orders/" + orderResp.orderId()))
                .body(orderResp);
    }

    // 내 주문 목록 조회
    @GetMapping("/v1/users/me/orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                orderService.getMyOrders(userDetails.getUsername())
        );
    }

    // 내 주문 상세 조회
    @GetMapping("/v1/users/me/orders/{orderId}")
    public ResponseEntity<OrderDetailResponse> getMyOrderDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(
                orderService.getMyOrderDetail(orderId, userDetails.getUsername())
        );
    }

    // 주문 취소
    @PostMapping("/v1/users/me/orders/{orderId}/cancel-request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> cancelOrderItems(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId,
            @RequestBody @Valid OrderCancelRequest req) {

        orderService.cancelOrderItems(userDetails.getUsername(), UUID.fromString(orderId), req);
        return ResponseEntity.noContent().build();
    }

    // 주문 확정
    @PostMapping("/v1/users/me/orders/{orderId}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> confirmOrderItems(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId,
            @RequestBody @Valid OrderConfirmRequest req) {
        orderService.confirmOrderItems(userDetails.getUsername(), orderId, req);
        return ResponseEntity.noContent().build();
    }

    // 결제 검증
    @PostMapping("/v1/payments/verify")
    public ResponseEntity<PaymentVerifyResponse> verifyPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PaymentVerifyRequest req) {

        PaymentVerifyResponse response =
                orderService.verifyPayment(userDetails.getUsername(), req);

        return ResponseEntity.ok(response);
    }
}
