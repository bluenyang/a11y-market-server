package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import jakarta.validation.constraints.NotEmpty;

public record OrderCancelRequest(@NotEmpty String orderItemId,
                                 @NotEmpty String reason) {
}
