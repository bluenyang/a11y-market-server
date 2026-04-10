package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import java.time.LocalDateTime
import java.util.*

data class ProductInquireResponse(
    val productId: UUID?,
    val productName: String?,
    val productPrice: Int?,
    val productStock: Int?,
    val productStatus: ProductStatus?,
    val categoryName: String?,
    val approvedAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    companion object {
        @JvmStatic
        fun fromEntity(product: Product): ProductInquireResponse {
            return ProductInquireResponse(
                product.productId,
                product.productName,
                product.productPrice,
                product.productStock,
                product.productStatus,
                product.category.categoryName,
                product.approvedDate,
                product.updatedAt
            )
        }
    }
}
