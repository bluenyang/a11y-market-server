package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.TossPaymentProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {
    private final TossPaymentProperties tossPaymentProperties;

    public void confirmPayment(String paymentKey, String orderId, int amount) {
        String encodedKey = Base64.getEncoder()
                .encodeToString((tossPaymentProperties.getSecretKey() + ":")
                        .getBytes());

        var restClient = RestClient.builder()
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .defaultHeader("Authorization", "Basic " + encodedKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

        Map<String, Object> body = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
        );

        try {
            restClient.post()
                    .uri("/confirm")
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Payment confirmation failed for paymentKey: {}. Error: {}", paymentKey, e.getMessage());
            throw new RuntimeException("결제 승인에 실패했습니다.");
        }
    }

    public void cancelPayment(String paymentKey, String reason, int cancelAmount) {
        String encodedKey = Base64.getEncoder()
                .encodeToString((tossPaymentProperties.getSecretKey() + ":")
                        .getBytes());

        var restClient = RestClient.builder()
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .defaultHeader("Authorization", "Basic " + encodedKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

        Map<String, Object> body = Map.of(
                "cancelReason", reason,
                "cancelAmount", cancelAmount
        );

        try {
            restClient.post()
                    .uri("/{paymentKey}/cancel", paymentKey)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Payment cancellation failed for paymentKey: {}. Error: {}", paymentKey, e.getMessage());
            throw new RuntimeException("결제 취소에 실패했습니다.");
        }
    }
}
