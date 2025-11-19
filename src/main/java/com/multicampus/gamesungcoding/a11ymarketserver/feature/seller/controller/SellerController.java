package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.SellerApplyRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.SellerApplyResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.SellerProductRegisterRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 판매자 관련 API 엔드포인트
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SellerController {

    private final SellerService sellerService;

    /**
     * 판매자 가입 신청
     * POST /api/v1/seller/apply
     */
    @PostMapping("/v1/seller/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SellerApplyResponse> applySeller(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid SellerApplyRequest request) {

        SellerApplyResponse response =
                sellerService.applySeller(userDetails.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 판매자 상품 등록 신청
     * POST /api/v1/seller/products
     */
    @PostMapping("/v1/seller/products")
    public ResponseEntity<ProductDTO> registerProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid SellerProductRegisterRequest request) {

        ProductDTO response = sellerService.registerProduct(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}