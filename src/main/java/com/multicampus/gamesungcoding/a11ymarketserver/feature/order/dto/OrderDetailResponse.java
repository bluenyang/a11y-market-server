package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;

import java.time.LocalDateTime;
import java.util.UUID;

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
        // OrderStatus orderStatus,
        Integer totalPrice,
        LocalDateTime createdAt,
        OrderItemResponse orderItem) {
    
    public static OrderDetailResponse fromEntity(OrderItems orderItem) {
        var item = OrderItemResponse.fromEntity(orderItem);
        var order = orderItem.getOrder();

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
                // order.getOrderStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                item
        );
    }
}
