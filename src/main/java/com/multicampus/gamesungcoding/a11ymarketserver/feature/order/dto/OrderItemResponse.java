package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;

import java.util.UUID;

public record OrderItemResponse(
        UUID orderItemId,
        UUID productId,
        String productName,
        Integer productPrice,
        Integer productQuantity,
        String orderItemStatus,
        String cancelReason
) {
    public static OrderItemResponse fromEntity(OrderItems entity) {
        return new OrderItemResponse(
                entity.getOrderItemId(),
                entity.getProductId(),
                entity.getProductName(),
                entity.getProductPrice(),
                entity.getProductQuantity(),
                entity.getOrderItemStatus().name(),
                entity.getCancelReason()
        );
    }
}
