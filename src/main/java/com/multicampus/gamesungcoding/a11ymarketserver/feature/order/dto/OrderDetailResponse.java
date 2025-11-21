package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record OrderDetailResponse(
        UUID orderId,
        String userName,
        String userEmail,
        String userPhone,
        String receiverName,
        String receiverPhone,
        String receiverZipcode,
        String receiverAddr1,
        String receiverAddr2,
        String orderStatus,
        Integer totalPrice,
        java.time.LocalDateTime createdAt,
        List<OrderItemResponse> orderItems
) {
    public static OrderDetailResponse fromEntity(Orders order, List<com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems> items) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList());

        return new OrderDetailResponse(
                order.getOrderId(),
                order.getUserName(),
                order.getUserEmail(),
                order.getUserPhone(),
                order.getReceiverName(),
                order.getReceiverPhone(),
                order.getReceiverZipcode(),
                order.getReceiverAddr1(),
                order.getReceiverAddr2(),
                order.getOrderStatus().name(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                itemResponses
        );
    }
}
