package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import java.util.List;

public record OrderCheckRequest(
        List<String> checkoutItemIds) {
}
