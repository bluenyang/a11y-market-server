package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper.toInquireResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper.toResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerApplyResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerInfoResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerProfileResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller

fun Seller.toApplyResponse(): SellerApplyResponse {
    return SellerApplyResponse(
        sellerId = this.sellerId ?: throw DataNotFoundException("Seller ID is missing"),
        sellerName = this.sellerName,
        userName = this.user.userName,
        userEmail = this.user.userEmail,
        userPhone = this.user.userPhone ?: throw DataNotFoundException("User phone is missing"),
        businessNumber = this.businessNumber,
        sellerGrade = this.sellerGrade,
        sellerIntro = this.sellerIntro,
        a11yGuarantee = this.isA11yGuarantee,
        sellerSubmitStatus = this.sellerSubmitStatus,
        submitDate = this.submitDate ?: throw DataNotFoundException("Submit date is missing"),
        approvedDate = this.approvedDate
    )
}

fun Seller.toInfoResponse(): SellerInfoResponse {
    return SellerInfoResponse(
        sellerId = this.sellerId ?: throw DataNotFoundException("Seller ID is missing"),
        sellerName = this.sellerName,
        businessNumber = this.businessNumber,
        sellerIntro = this.sellerIntro,
        sellerEmail = this.user.userEmail,
        sellerPhone = this.user.userPhone ?: throw DataNotFoundException("User phone is missing"),
        isA11yGuarantee = this.isA11yGuarantee,
        sellerGrade = this.sellerGrade,
        products = this.products.map { it.toResponse() }
    )
}

fun Seller.toProfileResponse(): SellerProfileResponse {
    return SellerProfileResponse(
        sellerId = this.sellerId ?: throw DataNotFoundException("Seller ID is missing"),
        sellerName = this.sellerName,
        businessNumber = this.businessNumber,
        sellerGrade = this.sellerGrade,
        contactEmail = this.user.userEmail,
        contactPhone = this.user.userPhone ?: throw DataNotFoundException("User phone is missing"),
        storeIntro = this.sellerIntro,
        isA11yGuarantee = this.isA11yGuarantee,
        profileStatus = this.sellerSubmitStatus,
        submitDate = this.submitDate ?: throw DataNotFoundException("Submit date is missing"),
        approvedDate = this.approvedDate,
        lastUpdatedDate = this.updatedAt ?: throw DataNotFoundException("Last updated date")
    )
}

fun Seller.toDetailResponse(ordersList: List<OrderItems>): SellerDetailResponse {
    return SellerDetailResponse(
        sellerId = this.sellerId ?: throw DataNotFoundException("Seller ID is missing"),
        sellerName = this.sellerName,
        businessNumber = this.businessNumber,
        sellerGrade = this.sellerGrade,
        contactEmail = this.user.userEmail,
        contactPhone = this.user.userPhone ?: throw DataNotFoundException("User phone is missing"),
        storeIntro = this.sellerIntro,
        isA11yGuarantee = this.isA11yGuarantee,
        profileStatus = this.sellerSubmitStatus,
        submitDate = this.submitDate ?: throw DataNotFoundException("Submit date is missing"),
        approvedDate = this.approvedDate,
        lastUpdatedDate = this.updatedAt ?: throw DataNotFoundException("Last updated date"),
        orders = ordersList.map { it.toOrderItemResponse() },
        products = this.products.map { it.toInquireResponse() }
    )
}
