package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.status.CheckExistsStatus;

public record CheckExistsResponse(CheckExistsStatus isAvailable) {
}
