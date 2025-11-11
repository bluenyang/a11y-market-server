package com.multicampus.gamesungcoding.a11ymarketserver.cart.dto;

import lombok.*;

import java.util.List;

@Builder
public record CartItemsResponse(List<CartDTO> items, int total) {
}