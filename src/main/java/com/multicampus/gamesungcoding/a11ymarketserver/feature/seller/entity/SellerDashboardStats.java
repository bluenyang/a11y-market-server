package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity;

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
@Table(name = "view_seller_dashboard_stats")
public class SellerDashboardStats {

    @Id
    @Column(columnDefinition = "RAW(16)")
    private UUID sellerId;

    @Column
    private BigDecimal totalRevenue;

    @Column
    private Long totalOrderCount;

    @Column
    private Long confirmedCount;

    @Column
    private Long refundedCount;
}
