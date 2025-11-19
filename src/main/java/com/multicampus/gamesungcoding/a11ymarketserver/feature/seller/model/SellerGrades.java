package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

import lombok.Getter;

public enum SellerGrades {
    NEWER("NEWER"),
    REGULAR("REGULAR"),
    TRUSTED("TRUSTED"),
    ;

    @Getter
    private final String grade;

    SellerGrades(String grade) {
        this.grade = grade;
    }
}
