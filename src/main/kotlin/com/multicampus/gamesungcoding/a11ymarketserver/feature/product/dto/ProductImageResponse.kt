package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages
import java.util.*

data class ProductImageResponse(
    val imageId: UUID,
    val imageUrl: String,
    val altText: String,
    val imageSequence: Int
) {
    companion object {
        @JvmStatic
        fun fromEntity(image: ProductImages): ProductImageResponse {
            return ProductImageResponse(
                image.imageId,
                image.imageUrl,
                image.altText,
                image.imageSequence
            )
        }
    }
}
