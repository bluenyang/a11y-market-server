package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import lombok.*;

import java.util.List;

@Builder
public record CartItemsResponse(List<CartDTO> items, int total) {
}