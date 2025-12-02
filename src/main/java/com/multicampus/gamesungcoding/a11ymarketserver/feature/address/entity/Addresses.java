package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @Embedded
    private AddressInfo address;

    @Column(nullable = false)
    private Boolean isDefault;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private Addresses(Users user, AddressInfo addressInfo, Boolean isDefault) {
        this.user = user;
        this.address = addressInfo;
        this.isDefault = isDefault;
    }

    // 배송지 정보 수정
    public void updateAddrInfo(AddressInfo addressInfo) {
        this.address = addressInfo;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
