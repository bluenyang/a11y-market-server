package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record SellerOrderStatusUpdateRequest(
        @NotNull OrderStatus status) {
}