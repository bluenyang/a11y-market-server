package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SellerOrderItemResponse(
        UUID orderItemId,
        UUID orderId,
        UUID productId,
        String productName,
        int productPrice,
        int productQuantity,
        OrderItemStatus orderItemStatus,
        OrderStatus orderStatus,
        String buyerName,
        String buyerEmail,
        String buyerPhone,
        LocalDateTime orderedAt
) {
    public static SellerOrderItemResponse fromEntity(OrderItems item) {
        return new SellerOrderItemResponse(
                item.getOrderItemId(),
                item.getOrder().getOrderId(),
                item.getProduct().getProductId(),
                item.getProductName(),
                item.getProductPrice(),
                item.getProductQuantity(),
                item.getOrderItemStatus(),
                item.getOrder().getOrderStatus(),
                item.getOrder().getUserName(),
                item.getOrder().getUserEmail(),
                item.getOrder().getUserPhone(),
                item.getOrder().getCreatedAt()
        );
    }
}
