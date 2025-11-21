package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model;

import lombok.Getter;

public enum ProductStatus {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    DELETED("DELETED");

    @Getter
    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }
}
