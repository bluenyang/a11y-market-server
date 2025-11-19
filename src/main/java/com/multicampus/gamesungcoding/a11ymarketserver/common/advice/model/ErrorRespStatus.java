package com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model;

import lombok.Getter;

@Getter
public enum ErrorRespStatus {
    USER_NOT_FOUND("USER_NOT_FOUND"),
    DATA_NOT_FOUND("DATA_NOT_FOUND"),
    DUPLICATED_DATA("DUPLICATED_DATA"),
    INVALID_REQUEST("INVALID_REQUEST"),
    ;

    private final String statusName;

    ErrorRespStatus(String statusName) {
        this.statusName = statusName;
    }
}
