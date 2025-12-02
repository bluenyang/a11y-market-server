package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto;

import jakarta.validation.constraints.NotNull;

public record DefaultAddressRequest(
        @NotNull(message = "addressId는 필수입니다.")
        String addressId) {
}

