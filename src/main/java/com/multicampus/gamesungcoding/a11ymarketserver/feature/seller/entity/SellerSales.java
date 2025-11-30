package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SELLER_SALES_VIEW")
public class SellerSales {

    @Id
    @Column(nullable = false, length = 16)
    private UUID sellerId;

    @Column
    private Integer totalSales;

    @Column
    private Integer totalOrders;

    @Column
    private Integer totalProductsSold;

    @Column
    private Integer totalCancelled;
}
