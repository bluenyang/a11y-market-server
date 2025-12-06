package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SellerDashboardResponse(
        UUID sellerId,
        String sellerName,
        BigDecimal totalRevenue,
        Long totalOrderCount,
        BigDecimal refundRate,
        BigDecimal confirmedRate) {
}
