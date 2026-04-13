package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*

/**
 * products 테이블 매핑.
 * - 컬럼명은 ERD(snake_case)와 @Column(name=...)로 1:1 매핑
 * - 식별자/감사필드는 외부 변경 금지(@Setter 없음)
 * - 값 변경은 의도 메서드로만 허용
 * - DTO 변환 책임 보유(toDTO)
 */
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener::class)
class Product(
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "seller_id", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var seller: Seller? = null,

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var category: Categories? = null,

    @Column(name = "product_price")
    var productPrice: Int,

    @Column(name = "product_stock")
    var productStock: Int,

    @Column(name = "product_name", nullable = false)
    var productName: String,

    @Column(name = "product_description", columnDefinition = "CLOB")
    var productDescription: String,

    @Column(name = "product_status", length = 50)
    @Enumerated(EnumType.STRING)
    var productStatus: ProductStatus
) {
    @Id
    @UuidV7
    @Column(nullable = false, updatable = false, length = 16)
    var productId: UUID? = null
        private set

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "submit_date", updatable = false)
    var submitDate: LocalDateTime? = null
        private set

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "approved_date", updatable = false)
    var approvedDate: LocalDateTime? = null
        private set

    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
        private set

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    var productImages: MutableList<ProductImages> = mutableListOf()
        private set

    /* === 의도 메서드 === */
    fun changeProductName(newProductName: String) {
        this.productName = newProductName
    }

    fun changePrice(newPrice: Int) {
        this.productPrice = newPrice
    }

    fun fillUpStock(amount: Int) {
        this.productStock += amount
    }

    fun changeStatus(newStatus: ProductStatus) {
        this.productStatus = newStatus
        if (newStatus.isApproved) {
            this.approvedDate = LocalDateTime.now()
        }
    }

    /**
     * 판매자가 자신의 상품을 수정할 때 사용하는 도메인 메서드
     * - 카테고리, 상품명, 설명, 가격, 재고 등을 수정
     * - 수정 시 관리자가 다시 검토해야 하므로 상태를 PENDING 으로 되돌림
     */
    fun updateBySeller(
        category: Categories,
        productName: String,
        productDescription: String,
        productPrice: Int,
        productStock: Int,
        productStatus: ProductStatus
    ) {
        this.category = category
        this.productName = productName
        this.productDescription = productDescription
        this.productPrice = productPrice
        this.productStock = productStock

        if (productStatus == ProductStatus.APPROVED) {
            // 상품 수정 시 관리자 승인 필요 → pending 상태로 전환
            this.productStatus = ProductStatus.PENDING
        } else {
            this.productStatus = productStatus
        }
    }

    /**
     * 판매자가 자신의 상품을 삭제할 때 사용하는 도메인 메서드
     * - 실제 DB 삭제가 아닌 상태만 DELETED 로 변경
     */
    fun deleteProduct() {
        this.productStatus = ProductStatus.DELETED
    }

    /**
     * 판매자가 자신의 상품 재고를 수정할 때 사용하는 도메인 메서드
     */
    fun updateStockBySeller(productStock: Int) {
        this.productStock = productStock
    }
}

