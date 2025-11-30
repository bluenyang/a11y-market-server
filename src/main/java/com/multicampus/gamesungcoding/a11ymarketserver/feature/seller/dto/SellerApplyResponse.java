package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;

import java.time.LocalDateTime;
import java.util.UUID;

public record SellerApplyResponse(
        UUID sellerId,
        String sellerName,
        String businessNumber,
        String sellerGrade,
        String sellerIntro,
        Boolean a11yGuarantee,
        String sellerSubmitStatus,
        LocalDateTime submitDate,
        LocalDateTime approvedDate) {

    public static SellerApplyResponse fromEntity(Seller seller) {
        return new SellerApplyResponse(
                seller.getSellerId(),
                seller.getSellerName(),
                seller.getBusinessNumber(),
                seller.getSellerGrade(),
                seller.getSellerIntro(),
                seller.getA11yGuarantee(),
                seller.getSellerSubmitStatus(),
                seller.getSubmitDate(),
                seller.getApprovedDate()
        );
    }

}
