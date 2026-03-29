package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto

import java.util.*

data class CartItemListDto(
    val sellerName: String,
    val sellerId: UUID,
    val groupTotal: Int,
    val items: List<CartItemDto>
)