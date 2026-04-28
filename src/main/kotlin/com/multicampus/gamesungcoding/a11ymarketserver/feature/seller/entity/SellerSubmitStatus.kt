package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity

enum class SellerSubmitStatus {
    PENDING,
    APPROVED,
    REJECTED;

    val isPending: Boolean
        get() = this == PENDING

    val isApproved: Boolean
        get() = this == APPROVED
}
