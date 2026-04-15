package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades
import java.time.LocalDateTime
import java.util.*

data class ProductAdminInquireResponse(
    val productId: UUID,
    val productName: String,
    val sellerId: UUID?,
    val sellerName: String?,
    val sellerGrade: SellerGrades?,
    val isA11yGuarantee: Boolean,
    val productPrice: Int,
    val productStatus: ProductStatus,
    val productDescription: String,
    val productStock: Int,
    val categoryId: UUID?,
    val categoryName: String?,
    val submitDate: LocalDateTime
) {
    // TODO: Admin 기능 migration 시 제거
    companion object {
        @JvmStatic
        fun fromEntity(product: Product): ProductAdminInquireResponse {
            val isA11yGuarantee = product.seller?.isA11yGuarantee ?: false

            return ProductAdminInquireResponse(
                product.productId,
                product.productName,
                product.seller?.sellerId,
                product.seller?.sellerName,
                product.seller?.sellerGrade,
                isA11yGuarantee,
                product.productPrice,
                product.productStatus,
                product.productDescription,
                product.productStock,
                product.category?.categoryId,
                product.category?.categoryName,
                product.submitDate
            )
        }
    }
}
