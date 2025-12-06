package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        // String userName,
        // String userEmail,
        // String userPhone,
        // String receiverName,
        // String receiverPhone,
        // String receiverZipcode,
        // String receiverAddr1,
        // String receiverAddr2,
        Integer totalPrice,
        List<OrderItemResponse> orderItems,
        // OrderStatus orderStatus,
        LocalDateTime createdAt) {

    public static OrderResponse fromEntity(Orders order, List<OrderItemResponse> orderItems) {
        return new OrderResponse(
                order.getOrderId(),
                // order.getUserName(),
                // order.getUserEmail(),
                // order.getUserPhone(),
                // order.getReceiverName(),
                // order.getReceiverPhone(),
                // order.getReceiverZipcode(),
                // order.getReceiverAddr1(),
                // order.getReceiverAddr2(),
                order.getTotalPrice(),
                orderItems,
                // order.getOrderStatus(),
                order.getCreatedAt()
        );
    }
}
