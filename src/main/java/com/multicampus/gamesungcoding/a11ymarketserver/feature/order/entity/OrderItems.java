package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.config.id.UuidV7;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ORDER_ITEMS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItems {

    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    private UUID orderItemId;

    @Column(length = 16, nullable = false)
    private UUID orderId;

    @Column(length = 16, nullable = false)
    private UUID productId;

    @Column(length = 255, nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long productPrice;

    @Column(nullable = false)
    private Integer productQuantity;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderItemStatus orderItemStatus;

    @Lob
    @Column
    private String cancelReason;

    public void assignOrderId(UUID orderId) {
        this.orderId = orderId;
    }

}
