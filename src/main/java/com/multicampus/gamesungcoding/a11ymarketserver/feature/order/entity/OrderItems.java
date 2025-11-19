package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ORDER_ITEMS")
public class OrderItems {

    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    private UUID orderItemId;

    @Column(length = 16, nullable = false)
    private UUID orderId;

    @Column(length = 16, nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer productPrice;

    @Column(nullable = false)
    private Integer productQuantity;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderItemStatus orderItemStatus;

    @Lob
    @Column
    private String cancelReason;

    @Builder
    private OrderItems(UUID orderId,
                       UUID productId,
                       String productName,
                       Integer productPrice,
                       Integer productQuantity,
                       OrderItemStatus orderItemStatus) {

        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.orderItemStatus = OrderItemStatus.ORDERED;
        this.cancelReason = null;
    }

    public void cancelOrderItem(String reason) {
        this.orderItemStatus = OrderItemStatus.CANCELED;
        this.cancelReason = reason;
    }
}
