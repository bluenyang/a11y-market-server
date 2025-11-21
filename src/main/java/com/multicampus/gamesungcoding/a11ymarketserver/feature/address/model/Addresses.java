package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Addresses {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    private UUID addressId;

    @Column(length = 16, updatable = false, nullable = false)
    private UUID userId;

    @Embedded
    private AddressInfo address;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private Addresses(UUID userId, AddressInfo addressInfo) {
        this.userId = userId;
        this.address = addressInfo;
    }

    // 배송지 정보 수정
    public void updateAddrInfo(AddressInfo addressInfo) {
        this.address = addressInfo;
    }
}
