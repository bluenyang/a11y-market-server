package com.multicampus.gamesungcoding.a11ymarketserver.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.address.model.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.address.model.DefaultAddress;
import com.multicampus.gamesungcoding.a11ymarketserver.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.address.repository.DefaultAddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.entity.CartItems;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.repository.CartItemRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.repository.CartRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.order.dto.AddressDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.order.dto.CartItemDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.order.dto.CheckoutInfoResponseDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//결제 정보 조회
@Service
@RequiredArgsConstructor
public class CheckoutInfoService {

    private final DefaultAddressRepository defaultAddressRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public CheckoutInfoResponseDTO getCheckoutInfo(UUID userId) {

        // 기본 배송지 조회
        DefaultAddress defaultAddress = defaultAddressRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("기본 배송지가 없습니다."));

        // 실제 주소 정보 조회
        Addresses address = addressRepository.findById(defaultAddress.getAddressId())
                .orElseThrow(() -> new IllegalStateException("기본 배송지 정보를 찾을 수 없습니다."));

        //장바구니 조회
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("장바구니가 존재하지 않습니다."));

        // 장바구니 상품 조회
        List<CartItems> cartItems = cartItemRepository.findByCartId(cart.getCartId()).stream().toList();

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어 있습니다.");
        }

        long totalAmount = 0;
        List<CartItemDTO> itemDTOs = new ArrayList<>();

        // 상품별 가격 계산
        for (CartItems item : cartItems) {
            Product p = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalStateException("상품 정보를 찾을 수 없습니다."));

            Integer qty = item.getQuantity();
            Long subtotal = p.getProductPrice().longValue() * qty;
            totalAmount += subtotal;

            itemDTOs.add(new CartItemDTO(
                    p.getProductId(),
                    p.getProductName(),
                    p.getProductPrice().longValue(),
                    item.getQuantity(),
                    subtotal
            ));
        }

        long shippingFee = 0;
        long finalAmount = totalAmount + shippingFee;

        // AddressDTO 변환
        AddressDTO addressDTO = new AddressDTO(
                address.getAddressId(),
                address.getReceiverName(),
                address.getReceiverPhone(),
                address.getReceiverZipcode(),
                address.getReceiverAddr1(),
                address.getReceiverAddr2()
        );

        // 최종 반환
        return new CheckoutInfoResponseDTO(
                addressDTO,
                itemDTOs,
                totalAmount,
                shippingFee,
                finalAmount
        );


    }


}
