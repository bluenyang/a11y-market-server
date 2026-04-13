package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity

enum class ProductStatus {
    PENDING,
    APPROVED,
    REJECTED,
    DELETED;

    val isApproved: Boolean
        get() = this == APPROVED
}
