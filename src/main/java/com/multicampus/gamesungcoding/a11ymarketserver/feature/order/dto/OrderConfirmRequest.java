package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import jakarta.validation.constraints.NotEmpty;

public record OrderConfirmRequest(
        @NotEmpty(message = "구매 확정할 상품을 선택해주세요.") String orderItemId
) {
}
