package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "carts")
public class Cart {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    private UUID cartId;

    @Column(length = 16, updatable = false, nullable = false)
    private UUID userId;

    @Builder
    private Cart(UUID userId) {
        this.userId = userId;
    }
}
