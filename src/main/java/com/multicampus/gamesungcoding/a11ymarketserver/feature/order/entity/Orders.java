package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ORDERS")
public class Orders {

    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    private UUID orderId;

    @Column(length = 30, nullable = false)
    private String userName;

    @Column(length = 150, nullable = false)
    private String userEmail;

    @Column(length = 15, nullable = false)
    private String userPhone;

    @Column(length = 30, nullable = false)
    private String receiverName;

    @Column(length = 15, nullable = false)
    private String receiverPhone;

    @Column(length = 5, columnDefinition = "CHAR(5)")
    private String receiverZipcode;

    @Column(length = 100, nullable = false)
    private String receiverAddr1;

    @Column(length = 200)
    private String receiverAddr2;

    @Column(nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private OrderStatus orderStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private Orders(
            String userName,
            String userEmail,
            String userPhone,
            String receiverName,
            String receiverPhone,
            String receiverZipcode,
            String receiverAddr1,
            String receiverAddr2,
            Integer totalPrice,
            OrderStatus orderStatus) {

        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverZipcode = receiverZipcode;
        this.receiverAddr1 = receiverAddr1;
        this.receiverAddr2 = receiverAddr2;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }


    public void updateTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
