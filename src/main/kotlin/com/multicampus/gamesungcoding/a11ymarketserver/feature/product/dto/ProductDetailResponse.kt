package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

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
    val summaryText: String? = null,
    val usageContext: String? = null,
    val submitDate: LocalDateTime,
    val usageMethod: String? = null
)
