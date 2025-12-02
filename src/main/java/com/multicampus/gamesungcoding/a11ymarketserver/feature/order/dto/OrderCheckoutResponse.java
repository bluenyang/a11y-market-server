package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderCheckoutStatus;

import java.util.List;
import java.util.UUID;

public record OrderCheckoutResponse(
        OrderCheckoutStatus status,
        Integer totalAmount,
        Integer shippingFee,
        Integer finalAmount,
        List<AddressResponse> addresses,
        UUID defaultAddressId) {
}
