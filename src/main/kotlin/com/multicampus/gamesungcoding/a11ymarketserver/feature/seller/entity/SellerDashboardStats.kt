package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.math.BigDecimal
import java.util.*

@Entity
@Immutable
@Table(name = "view_seller_dashboard_stats")
class SellerDashboardStats(
    @Id
    @Column
    var sellerId: UUID,

    @Column
    var totalRevenue: BigDecimal,

    @Column
    var totalOrderCount: Long,

    @Column
    var confirmedCount: Long,

    @Column
    var refundedCount: Long
)
