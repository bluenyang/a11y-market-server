package com.multicampus.gamesungcoding.a11ymarketserver.order.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.order.dto.CheckoutInfoResponseDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.order.dto.OrderCreateReqDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.order.service.CheckoutInfoService;
import com.multicampus.gamesungcoding.a11ymarketserver.order.service.OrderCreateService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class OrderController {

    private final CheckoutInfoService checkoutInfoService;
    private final OrderCreateService orderCreateService;

    //결제 준비 (결제 정보 조회)
    @PostMapping("/v1/orders/pre-check")
    public CheckoutInfoResponseDTO preCheck(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        return checkoutInfoService.getCheckoutInfo(userId);
    }

    // 주문 생성
    @PostMapping("v1/orders")
    public UUID createOrder(HttpSession session, @Valid @RequestBody OrderCreateReqDTO req) {
        UUID userId = (UUID) session.getAttribute("userId");
        return orderCreateService.createOrder(userId, req);
    }
}
