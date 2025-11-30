package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/v1/seller/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SellerApplyResponse> applySeller(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid SellerApplyRequest request) {

        SellerApplyResponse response = sellerService.applySeller(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/v1/seller/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDetailResponse> registerProduct(
            @AuthenticationPrincipal
            UserDetails userDetails,

            @Valid
            @RequestPart("data")
            @Parameter(
                    description = "Product registration data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            SellerProductRegisterRequest request,

            @RequestPart(value = "images", required = false)
            List<MultipartFile> images
    ) {

        var response = sellerService.registerProduct(userDetails.getUsername(), request, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/v1/seller/products")
    public ResponseEntity<List<ProductDTO>> getMyProducts(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<ProductDTO> products = sellerService.getMyProducts(userDetails.getUsername());
        return ResponseEntity.ok(products);
    }


    @PutMapping("/v1/seller/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String productId,
            @RequestBody @Valid SellerProductUpdateRequest request) {

        ProductDTO result = sellerService.updateProduct(userDetails.getUsername(), UUID.fromString(productId), request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/v1/seller/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String productId) {

        sellerService.deleteProduct(userDetails.getUsername(), UUID.fromString(productId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/v1/seller/products/{productId}/stock")
    public ResponseEntity<ProductDTO> updateProductStock(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String productId,
            @RequestBody @Valid SellerProductStockUpdateRequest request) {

        ProductDTO result = sellerService.updateProductStock(userDetails.getUsername(), UUID.fromString(productId), request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/v1/seller/orders")
    public ResponseEntity<List<SellerOrderItemResponse>> getReceivedOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String status) {

        List<SellerOrderItemResponse> responses = sellerService.getReceivedOrders(userDetails.getUsername(), status);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/v1/seller/orders/{orderId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateOrderStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId,
            @RequestBody @Valid SellerOrderStatusUpdateRequest request) {

        sellerService.updateOrderStatus(userDetails.getUsername(), UUID.fromString(orderId), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/v1/seller/claims/{claimId}/approve")
    public ResponseEntity<Void> processOrderClaim(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String claimId,
            @RequestBody @Valid SellerOrderClaimProcessRequest request) {

        sellerService.processOrderClaim(userDetails.getUsername(), UUID.fromString(claimId), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/seller/claims")
    public ResponseEntity<List<SellerOrderItemResponse>> getOrderClaims(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<SellerOrderItemResponse> claims = sellerService.getOrderClaims(userDetails.getUsername());
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/v1/seller/dashboard")
    public ResponseEntity<SellerDashboardResponse> getDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {

        SellerDashboardResponse response = sellerService.getDashboard(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}