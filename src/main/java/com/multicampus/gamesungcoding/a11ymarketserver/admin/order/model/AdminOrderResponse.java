package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
public record AdminOrderResponse(
        UUID orderId,
        String userName,
        String userEmail,
        String userPhone,
        String receiverName,
        String receiverPhone,
        String receiverZipcode,
        String receiverAddr1,
        String receiverAddr2,
        OrderStatus orderStatus,
        Integer totalPrice,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime createdAt) {

    public static AdminOrderResponse fromEntity(Orders entity) {
        log.debug("AdminOrderResponse - fromEntity: Converting Orders entity to AdminOrderResponse DTO");
        return new AdminOrderResponse(
                entity.getOrderId(),
                entity.getUserName(),
                entity.getUserEmail(),
                entity.getUserPhone(),
                entity.getReceiverName(),
                entity.getReceiverPhone(),
                entity.getReceiverZipcode(),
                entity.getReceiverAddr1(),
                entity.getReceiverAddr2(),
                entity.getOrderStatus(),
                entity.getTotalPrice(),
                entity.getCreatedAt()
        );
    }
}
