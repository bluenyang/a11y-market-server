package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SellerProductStockUpdateRequest(

        @NotNull(message = "재고 수량은 필수입니다.")
        @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
        Integer productStock) {
}