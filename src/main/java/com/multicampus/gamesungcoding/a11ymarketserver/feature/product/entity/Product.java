package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * products 테이블 매핑.
 * - 컬럼명은 ERD(snake_case)와 @Column(name=...)로 1:1 매핑
 * - 식별자/감사필드는 외부 변경 금지(@Setter 없음)
 * - 값 변경은 의도 메서드로만 허용
 * - DTO 변환 책임 보유(toDTO)
 */
@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @UuidV7
    @Column(nullable = false, updatable = false, length = 16)
    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", updatable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categories category;

    @Column(name = "product_price")
    private Integer productPrice;

    @Column(name = "product_stock")
    private Integer productStock;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description", columnDefinition = "CLOB")
    private String productDescription;

    // 이전에 있던 컬럼. 현재는 사용되지 않음.
    @Column(name = "product_ai_summary", columnDefinition = "CLOB")
    private String productAiSummary;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", length = 50)
    private ProductStatus productStatus;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "submit_date", updatable = false)
    private LocalDateTime submitDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "approved_date", updatable = false)
    private LocalDateTime approvedDate;

    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImages> productImages;

    @Builder
    public Product(Seller seller,
                   Categories category,
                   Integer productPrice,
                   Integer productStock,
                   String productName,
                   String productDescription,
                   ProductStatus productStatus) {

        this.seller = seller;
        this.category = category;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productStatus = productStatus;
        this.productImages = new ArrayList<>();
    }

    /* === 의도 메서드 === */
    public void changeProductName(String newProductName) {
        this.productName = newProductName;
    }

    public void changePrice(Integer newPrice) {
        this.productPrice = newPrice;
    }

    public void fillUpStock(int amount) {
        this.productStock = (this.productStock == null ? 0 : this.productStock) + amount;
    }

    public void changeStatus(ProductStatus newStatus) {
        this.productStatus = newStatus;
        if (newStatus.isApproved()) {
            this.approvedDate = LocalDateTime.now();
        }
    }

    /**
     * 판매자가 자신의 상품을 수정할 때 사용하는 도메인 메서드
     * - 카테고리, 상품명, 설명, 가격, 재고 등을 수정
     * - 수정 시 관리자가 다시 검토해야 하므로 상태를 PENDING 으로 되돌림
     */
    public void updateBySeller(
            Categories category,
            String productName,
            String productDescription,
            Integer productPrice,
            Integer productStock) {

        this.category = category;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStock = productStock;

        // 상품 수정 시 관리자 승인 필요 → pending 상태로 전환
        this.productStatus = ProductStatus.PENDING;
    }

    /**
     * 판매자가 자신의 상품을 삭제할 때 사용하는 도메인 메서드
     * - 실제 DB 삭제가 아닌 상태만 DELETED 로 변경
     */
    public void deleteBySeller() {
        this.productStatus = ProductStatus.DELETED;
    }

    /**
     * 판매자가 자신의 상품 재고를 수정할 때 사용하는 도메인 메서드
     */
    public void updateStockBySeller(Integer productStock) {
        this.productStock = productStock;
    }
}

