package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.DailyRevenueDto;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerDashboardService;
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
    private final SellerDashboardService sellerDashboardService;

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

    @GetMapping("/v1/seller/orders/items")
    public ResponseEntity<List<SellerOrderItemResponse>> getReceivedOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) OrderItemStatus orderItemStatus) {

        var responses = sellerService.getReceivedOrders(userDetails.getUsername(), orderItemStatus);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/v1/seller/orders/items/{orderItemId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateOrderStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderItemId,
            @RequestBody @Valid SellerOrderItemsStatusUpdateRequest request) {

        sellerService.updateOrderItemStatus(
                userDetails.getUsername(),
                UUID.fromString(orderItemId),
                request);
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

    @GetMapping("/v1/seller/dashboard/stats")
    public ResponseEntity<SellerDashboardResponse> getDashboardStats(
            @AuthenticationPrincipal UserDetails userDetails) {

        SellerDashboardResponse response = sellerDashboardService.getDashboard(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/seller/dashboard/daily-revenue")
    public ResponseEntity<List<DailyRevenueDto>> getDailyRevenue(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "2025") int year,
            @RequestParam(defaultValue = "12") int month) {

        List<DailyRevenueDto> dailyRevenues =
                sellerDashboardService.getDailyRevenue(
                        userDetails.getUsername(), year, month);
        return ResponseEntity.ok(dailyRevenues);
    }

    @GetMapping("/v1/seller/dashboard/top-products")
    public ResponseEntity<List<SellerTopProductResponse>> getTopProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int topN) {

        List<SellerTopProductResponse> topProducts =
                sellerDashboardService.getTopProducts(userDetails.getUsername(), topN);
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/v1/seller/dashboard/recent-orders")
    public ResponseEntity<List<SellerOrderItemResponse>> getRecentOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Fetching recent orders for seller: {}, page: {}, size: {}",
                userDetails.getUsername(), page, size);

        List<SellerOrderItemResponse> recentOrders =
                sellerDashboardService.getRecentOrders(
                        userDetails.getUsername(), page, size);

        return ResponseEntity.ok(recentOrders);
    }
}