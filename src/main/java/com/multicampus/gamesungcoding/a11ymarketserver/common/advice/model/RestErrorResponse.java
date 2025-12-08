package com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model;

import org.springframework.http.HttpStatus;

public record RestErrorResponse(HttpStatus status,
                                ErrorRespStatus error,
                                String message) {
}
