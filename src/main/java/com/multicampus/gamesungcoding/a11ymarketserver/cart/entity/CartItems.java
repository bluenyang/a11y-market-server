package com.multicampus.gamesungcoding.a11ymarketserver.cart.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID cartItemId;                 // 장바구니 아이템 PK

    @Column(nullable = false)
    private UUID cartId;        // 사용자/회원 ID

    @Column(nullable = false)
    private UUID productId;          // 상품 ID

    @Column(nullable = false)
    private Integer quantity;        // 수량

    public void changeQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be >= 1");
        }
        this.quantity = quantity;
    }
}