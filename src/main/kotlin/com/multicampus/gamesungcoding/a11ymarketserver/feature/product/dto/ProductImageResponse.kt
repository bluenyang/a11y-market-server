package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import java.util.*

data class ProductImageResponse(
    val imageId: UUID,
    val imageUrl: String,
    val altText: String,
    val imageSequence: Int
)
