package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import javax.validation.constraints.NotNull;
import java.util.List;

public record PaymentVerifyRequest(

        @NotNull(message = "주문 ID는 필수입니다.")
        String orderId,

        @NotNull(message = "결제 금액은 필수입니다.")
        @Positive(message = "결제 금액은 0보다 커야 합니다.")
        Integer amount,

        @NotBlank(message = "결제 방식은 필수입니다.")
        String method,

        String paymentKey,
        
        String impUid,

        List<String> cartItemIdsToDelete) {
}
