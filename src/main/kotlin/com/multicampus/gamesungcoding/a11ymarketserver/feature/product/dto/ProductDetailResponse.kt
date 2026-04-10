package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductAiSummary
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades
import java.time.LocalDateTime
import java.util.*

data class ProductDetailResponse(
    val productId: UUID,
    val productName: String,
    val sellerId: UUID,
    val sellerName: String,
    val sellerGrade: SellerGrades,
    val isA11yGuarantee: Boolean,
    val productPrice: Int,
    val productStatus: ProductStatus,
    val productDescription: String,
    val productStock: Int,
    val productImages: List<ProductImageResponse>,
    val categoryId: UUID,
    val categoryName: String,
    val summaryText: String,
    val usageContext: String,
    val submitDate: LocalDateTime,
    val usageMethod: String
) {
    companion object {
        fun fromEntity(
            product: Product,
            images: List<ProductImages>,
            summary: ProductAiSummary
        ): ProductDetailResponse {
            val images = images
            val seller = product.seller
            val category = product.category

            return ProductDetailResponse(
                product.productId,
                product.productName,
                seller.sellerId,
                seller.sellerName,
                seller.sellerGrade,
                seller.isA11yGuarantee,
                product.productPrice,
                product.productStatus,
                product.productDescription,
                product.productStock,
                images.map { ProductImageResponse.fromEntity(it) },
                category.categoryId,
                category.categoryName,
                summary.summaryText,
                summary.usageContext,
                product.submitDate,
                summary.usageMethod
            )
        }
    }
}
