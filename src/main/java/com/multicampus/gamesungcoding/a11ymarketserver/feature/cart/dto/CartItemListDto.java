package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import java.util.List;
import java.util.UUID;

public record CartItemListDto(String sellerName,
                              UUID sellerId,
                              Integer groupTotal,
                              List<CartItemDto> items) {
}
