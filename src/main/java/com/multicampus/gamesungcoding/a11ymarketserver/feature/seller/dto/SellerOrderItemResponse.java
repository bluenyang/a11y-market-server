package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;

import java.time.LocalDateTime;
import java.util.UUID;

public record SellerOrderItemResponse(
        UUID orderItemId,
        UUID orderId,
        UUID productId,
        String productName,
        int productPrice,
        int productQuantity,
        String orderItemStatus,
        String orderStatus,
        String buyerName,
        String buyerEmail,
        String buyerPhone,
        LocalDateTime orderedAt
) {
    public SellerOrderItemResponse(Orders order, OrderItems item) {
        this(
                item.getOrderItemId(),
                order.getOrderId(),
                item.getProductId(),
                item.getProductName(),
                item.getProductPrice(),
                item.getProductQuantity(),
                item.getOrderItemStatus().name(),
                order.getOrderStatus().name(),
                order.getUserName(),
                order.getUserEmail(),
                order.getUserPhone(),
                order.getCreatedAt()
        );
    }
}
