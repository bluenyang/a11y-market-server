package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import lombok.Getter;

public enum SellerSubmitStatus {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    @Getter
    private final String status;

    SellerSubmitStatus(String status) {
        this.status = status;
    }
}
