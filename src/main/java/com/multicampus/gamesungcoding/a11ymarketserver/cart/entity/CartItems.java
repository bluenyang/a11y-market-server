package com.multicampus.gamesungcoding.a11ymarketserver.cart.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.config.id.UuidV7;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Integer quantity;

    public void changeQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be >= 1");
        }
        this.quantity = quantity;
    }
}