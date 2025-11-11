package com.multicampus.gamesungcoding.a11ymarketserver.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartAddRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID productId;

    @NotNull
    @Min(1)
    private Integer quantity;
}