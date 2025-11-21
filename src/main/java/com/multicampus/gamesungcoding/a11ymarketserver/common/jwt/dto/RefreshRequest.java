package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshRequest(@NotNull String refreshToken) {
}
