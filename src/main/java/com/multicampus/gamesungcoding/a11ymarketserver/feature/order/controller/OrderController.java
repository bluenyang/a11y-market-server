package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCheckRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCheckoutResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCreateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class OrderController {

    private final OrderService orderService;

    // 결제 준비 (결제 정보 조회)
    @PostMapping("/v1/orders/pre-check")
    public OrderCheckoutResponse preCheck(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderCheckRequest req) {
        
        return orderService.getCheckoutInfo(userDetails.getUsername(), req);
    }

    // 주문 생성
    @PostMapping("v1/orders")
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderCreateRequest req) {

        var orderResp = orderService.createOrder(userDetails.getUsername(), req);
        return ResponseEntity
                .created(URI.create("/api/v1/users/me/orders/" + orderResp.orderId()))
                .body(orderResp);
    }
}
