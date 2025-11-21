package com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model;

public record RestErrorResponse(ErrorRespStatus error,
                                String message) {
}
