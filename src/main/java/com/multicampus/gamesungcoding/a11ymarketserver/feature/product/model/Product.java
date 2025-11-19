package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Product {

    /**
     * PK: UUID 36자
     */
    @Id
    @UuidV7
    @Column(nullable = false, updatable = false, length = 16)
    private UUID productId;

    /**
     * FK: 판매자
     */
    @Column(name = "seller_id", length = 16)
    private UUID sellerId;

    /**
     * FK: 카테고리
     */
    @Column(name = "category_id", length = 16)
    private UUID categoryId;

    /**
     * 가격 (NULL 허용 → Integer)
     */
    @Column(name = "product_price")
    private Integer productPrice;

    /**
     * 재고
     */
    @Column(name = "product_stock")
    private Integer productStock;

    /**
     * 상품명
     */
    @Column(name = "product_name", length = 255, nullable = false)
    private String productName;

    /**
     * 상세설명
     */
    @Column(name = "product_description", columnDefinition = "CLOB")
    private String productDescription;

    /**
     * AI 요약
     */
    @Column(name = "product_ai_summary", columnDefinition = "CLOB")
    private String productAiSummary;

    /**
     * 상태 (예: AVAILABLE, SOLD_OUT)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", length = 50)
    private ProductStatus productStatus;

    /**
     * 생성/수정 시각 (JPA Auditing)
     */
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

    /**
     * 저장 직전 UUID 자동 생성 (prefix 없음)
     */
    @PrePersist
    private void prePersist() {
        if (this.productId == null) {
            this.productId = UUID.randomUUID();
        }
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
    }
}

