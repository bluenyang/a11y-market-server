package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Immutable
@Table(name = "view_monthly_popular_products")
public class MonthlyPopularProduct {

    @Id
    @Column(columnDefinition = "RAW(16)")
    UUID productId;

    @Column
    private String productName;

    @Column
    private BigDecimal productPrice;

    @Column
    private String productImageUrl;

    @Column(columnDefinition = "RAW(16)")
    private UUID categoryId;

    @Column
    private String categoryName;

    @Column(columnDefinition = "RAW(16)")
    private UUID sellerId;

    @Column
    private Long monthlySalesVolume;

    @Column
    private Long monthlyOrderCount;

    @Column
    private Integer ranking;
}
