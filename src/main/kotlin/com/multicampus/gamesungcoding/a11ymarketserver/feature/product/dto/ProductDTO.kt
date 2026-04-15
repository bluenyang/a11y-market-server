package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import java.time.LocalDateTime
import java.util.*

/**
 * 상품 목록/검색 응답 DTO.
 * - 엔티티 직접 참조/의존 없음
 * - 현재 스코프에 필요한 최소 필드만 노출
 */
data class ProductDTO(
    private var productId: UUID,
    private var productName: String,
    private var productPrice: Int,
    private var productStatus: ProductStatus,
    private var submitDate: LocalDateTime
) {

    // TODO: Seller 기능 변환시 삭제
    companion object {
        @JvmStatic
        fun fromEntity(entity: Product): ProductDTO {
            return ProductDTO(
                entity.productId,
                entity.productName,
                entity.productPrice,
                entity.productStatus,
                entity.submitDate
            )
        }
    }
}
