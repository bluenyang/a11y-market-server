package com.multicampus.gamesungcoding.a11ymarketserver.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartQtyUpdateRequest {
    @NotNull
    @Min(1)
    private Integer quantity;
}