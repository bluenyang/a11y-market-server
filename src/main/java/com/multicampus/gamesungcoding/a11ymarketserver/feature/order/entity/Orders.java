package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    // PG사 결제 고유 키 - 환불 처리 시 필요
    @Column(length = 200)
    private String paymentKey;

    /**
     * @deprecated : 필요하지 않지만, 향후 필요성을 알 수 없어 남겨둠
     */
    @Deprecated
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private final OrderStatus orderStatus = OrderStatus.DEPRECATED;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order")
    private List<OrderItems> orderItems = new ArrayList<>();

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
            Integer totalPrice) {

        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverZipcode = receiverZipcode;
        this.receiverAddr1 = receiverAddr1;
        this.receiverAddr2 = receiverAddr2;
        this.totalPrice = totalPrice;
    }

    public void updateTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Deprecated
    public void updateOrderItemStatus() {
        // this.orderStatus = orderStatus;
    }

    public void updatePaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }
}
