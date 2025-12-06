package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "view_seller_top_products")
public class SellerTopProduct {

    @EmbeddedId
    private SellerTopProductId id;

    @Column
    private String productName;

    @Column
    private BigDecimal productPrice;

    @Column
    private String productImageUrl;

    @Column
    private Long orderCount;

    @Column
    private Long totalQuantitySold;

    @Column
    private BigDecimal totalSalesAmount;

    @Column
    private Integer salesRank;
}
