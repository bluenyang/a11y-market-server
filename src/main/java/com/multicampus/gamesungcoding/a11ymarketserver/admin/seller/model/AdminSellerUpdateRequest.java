package com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.model;

public record AdminSellerUpdateRequest(
        String sellerName,
        String businessNumber,
        SellerGrades sellerGrade,
        String sellerIntro,
        Boolean a11yGuarantee) {
}
