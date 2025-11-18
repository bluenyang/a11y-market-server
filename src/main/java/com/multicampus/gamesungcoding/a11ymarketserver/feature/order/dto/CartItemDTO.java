package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private UUID productID;
    private String productName;
    private Long price; // 현재 가격
    private Integer quantity;
    private Long subtotal; // price * quantity
}
