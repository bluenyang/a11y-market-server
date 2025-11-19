package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity;

import lombok.Getter;

public enum OrderCheckoutStatus {
    AVAILABLE("AVAILABLE"),
    OUT_OF_STOCK("OUT_OF_STOCK");

    @Getter
    private final String status;

    OrderCheckoutStatus(String status) {
        this.status = status;
    }
}
