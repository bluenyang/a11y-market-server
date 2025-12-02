package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity;

public enum ProductStatus {
    PENDING,
    APPROVED,
    REJECTED,
    DELETED;

    public boolean isApproved() {
        return this == APPROVED;
    }
}
