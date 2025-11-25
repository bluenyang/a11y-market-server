package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import jakarta.validation.constraints.NotNull;

public record SellerOrderClaimProcessRequest(
        @NotNull String action) {
}