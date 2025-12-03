package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import java.util.UUID;

public record SellerDashboardResponse(
        UUID sellerId,
        String sellerName,
        int totalSales,
        int totalOrders,
        int totalProductsSold,
        int totalCancelled) {
}
