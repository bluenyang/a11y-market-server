package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItems {

    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    private UUID cartItemId;                 // 장바구니 아이템 PK

    @Column(nullable = false)
    private UUID cartId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private int quantity;

    @Builder
    private CartItems(UUID cartId, UUID productId, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void changeQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be >= 1");
        }
        this.quantity = quantity;
    }
}