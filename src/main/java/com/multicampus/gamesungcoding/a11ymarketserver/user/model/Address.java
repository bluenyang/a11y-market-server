package com.multicampus.gamesungcoding.a11ymarketserver.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "address")
@EntityListeners(AuditingEntityListener.class)

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 16, updatable = false, nullable = false)
    private UUID addressId;

    @Column(length = 16, updatable = false, nullable = false)
    private UUID userId;

    @Column(length = 30)
    private String receiverName;

    @Column(length = 13)
    private String receiverPhone;

    private Integer receiverZipcode;

    @Column(length = 100)
    private String receiverAddr1;

    @Column(length = 200)
    private String receiverAddr2;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 배송지 정보 수정
    public void updateAddrInfo(AddressRequest request) {
        if (request.getReceiverName() != null) {
            this.receiverName = request.getReceiverName();
        }
        if (request.getReceiverPhone() != null) {
            this.receiverPhone = request.getReceiverPhone();
        }
        if (request.getReceiverZipcode() != null) {
            this.receiverZipcode = request.getReceiverZipcode();
        }
        if (request.getReceiverAddr1() != null) {
            this.receiverAddr1 = request.getReceiverAddr1();
        }
        if (request.getReceiverAddr2() != null) {
            this.receiverAddr2 = request.getReceiverAddr2();
        }
    }
}
