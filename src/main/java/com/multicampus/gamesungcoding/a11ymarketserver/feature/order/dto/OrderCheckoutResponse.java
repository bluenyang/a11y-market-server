package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderCheckoutStatus;

public record OrderCheckoutResponse(
        OrderCheckoutStatus status,
        Integer totalAmount,
        Integer shippingFee,
        Integer finalAmount) {
}
