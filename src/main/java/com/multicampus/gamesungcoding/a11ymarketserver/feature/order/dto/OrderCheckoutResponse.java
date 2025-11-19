package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

/**
 * 결제 정보 응답 DTO
 *
 * @param status      체크아웃 상태
 * @param totalAmount 총 상품 금액
 * @param shippingFee 배송비 (지금은 0)
 * @param finalAmount totalAmount + shippingFee
 */
public record OrderCheckoutResponse(String status, Integer totalAmount, Integer shippingFee, Integer finalAmount) {
}
