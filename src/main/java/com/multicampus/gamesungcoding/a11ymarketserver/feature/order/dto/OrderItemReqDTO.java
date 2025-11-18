package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemReqDTO {

    @NotNull
    private String productId;

    @Min(1)
    private Integer quantity;
}
