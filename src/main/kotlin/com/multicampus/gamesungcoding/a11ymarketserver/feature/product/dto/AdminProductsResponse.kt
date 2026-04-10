package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

data class AdminProductsResponse(
    val totalCount: Int,
    val products: List<ProductAdminInquireResponse>
)
