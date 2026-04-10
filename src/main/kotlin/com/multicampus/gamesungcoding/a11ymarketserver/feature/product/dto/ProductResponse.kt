package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import java.util.*

data class ProductResponse(
    val productId: UUID,
    val productName: String,
    val productDescription: String,
    val sellerName: String,
    val isA11yGuarantee: Boolean,
    val productPrice: Int,
    val productImages: List<ProductImageResponse?>,
    val parentCategoryId: UUID,
    val categoryId: UUID,
    val categoryName: String
) {
    companion object {
        @JvmStatic
        fun fromEntity(product: Product): ProductResponse {
            val seller = product.seller
            val category = product.category
            return ProductResponse(
                product.productId,
                product.productName,
                product.productDescription,
                seller.sellerName,
                seller.isA11yGuarantee,
                product.productPrice,
                product.productImages.map { ProductImageResponse.fromEntity(it) },
                category.parentCategory.categoryId,
                category.categoryId,
                category.categoryName
            )
        }
    }
}
