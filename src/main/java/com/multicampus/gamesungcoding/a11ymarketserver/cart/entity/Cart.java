package com.multicampus.gamesungcoding.a11ymarketserver.cart.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.config.id.UuidV7;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    private UUID cartId;

    @Column(length = 16, updatable = false, nullable = false)
    private UUID userId;
}
