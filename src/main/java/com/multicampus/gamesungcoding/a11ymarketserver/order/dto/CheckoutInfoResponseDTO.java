package com.multicampus.gamesungcoding.a11ymarketserver.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutInfoResponseDTO {
    private AddressDTO address; //기본 배송지
    private List<CartItemDTO> cartItems; //장바구니 목록
    private Long totalAmount; //총 상품 금액
    private Long shippingFee; //배송비 (지금은 0)
    private Long finalAmount; //totalAmount + shippingFee


}
