package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.DailyRevenueDto
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductInquireResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerDashboardService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/v1/seller")
class SellerController(
    private val sellerDashboardService: SellerDashboardService,
    private val sellerService: SellerService
) {

    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.CREATED)
    fun applySeller(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody @Valid request: SellerApplyRequest
    ): SellerApplyResponse = sellerService.applySeller(userDetails.username, request)

    @PutMapping("/me")
    fun updateSellerInfo(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody @Valid request: SellerUpdateRequest
    ): SellerInfoResponse = sellerService.updateSellerInfo(userDetails.username, request)

    @PostMapping(value = ["/products"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun registerProduct(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestPart("data") @Parameter(
            description = "Product registration data",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
        ) request: SellerProductRegisterRequest,
        @RequestPart(value = "images", required = false) images: List<MultipartFile>?
    ): ProductDetailResponse = sellerService.registerProduct(userDetails.username, request, images)

    @GetMapping("/products")
    fun getMyProducts(
        @AuthenticationPrincipal userDetails: UserDetails,
        @ModelAttribute req: SellerInquireProductRequest
    ): List<ProductInquireResponse> = sellerService.getMyProducts(userDetails.username, req)

    @PutMapping(path = ["/products/{productId}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateProduct(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable productId: String,
        @Valid @RequestPart("data") @Parameter(
            description = "Product registration data",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
        ) request: SellerProductUpdateRequest,
        @RequestPart(value = "images", required = false) images: List<MultipartFile>?
    ): ProductDTO =
        sellerService.updateProduct(
            userDetails.username,
            UUID.fromString(productId),
            request,
            images
        )

    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable productId: String
    ) {
        sellerService.deleteProduct(userDetails.username, UUID.fromString(productId))
    }

    @PatchMapping("/products/{productId}/stock")
    fun updateProductStock(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable productId: String,
        @RequestBody @Valid request: SellerProductStockUpdateRequest
    ): ProductDTO = sellerService.updateProductStock(userDetails.username, UUID.fromString(productId), request)

    @GetMapping("/orders/items")
    fun getReceivedOrders(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) size: Int?,
        @RequestParam(required = false) orderItemStatus: OrderItemStatus?
    ): SellerOrderInquireResponse =
        sellerService.getReceivedOrders(userDetails.username, orderItemStatus, page, size)

    @GetMapping("/orders/summary")
    fun getOrderSummary(
        @AuthenticationPrincipal userDetails: UserDetails
    ): SellerOrderSummaryResponse = sellerService.getOrderSummary(userDetails.username)

    @PatchMapping("/orders/items/{orderItemId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateOrderStatus(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable orderItemId: String,
        @RequestBody @Valid request: SellerOrderItemsStatusUpdateRequest
    ) {
        sellerService.updateOrderItemStatus(
            userDetails.username,
            UUID.fromString(orderItemId),
            request
        )
    }

    @PostMapping("/claims/{claimId}/approve")
    fun processOrderClaim(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable claimId: String,
        @RequestBody @Valid request: SellerOrderClaimProcessRequest
    ) {
        sellerService.processOrderClaim(
            userDetails.username,
            UUID.fromString(claimId),
            request
        )
    }

    @GetMapping("/claims")
    fun getOrderClaims(
        @AuthenticationPrincipal userDetails: UserDetails
    ): List<SellerOrderItemResponse> = sellerService.getOrderClaims(userDetails.username)

    @GetMapping("/dashboard/stats")
    fun getDashboardStats(
        @AuthenticationPrincipal userDetails: UserDetails
    ): SellerDashboardResponse = sellerDashboardService.getDashboard(userDetails.username)

    @GetMapping("/dashboard/daily-revenue")
    fun getDailyRevenue(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam(defaultValue = "2025") year: Int,
        @RequestParam(defaultValue = "12") month: Int
    ): List<DailyRevenueDto> = sellerDashboardService.getDailyRevenue(userDetails.username, year, month)

    @GetMapping("/dashboard/top-products")
    fun getTopProducts(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam topN: Int
    ): List<SellerTopProductResponse> =
        sellerDashboardService.getTopProducts(userDetails.username, topN)

    @GetMapping("/dashboard/recent-orders")
    fun getRecentOrders(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): List<SellerOrderItemResponse> =
        sellerDashboardService.getRecentOrders(userDetails.username, page, size)

    @GetMapping("/info/{sellerId}")
    fun getSellerInfo(
        @PathVariable sellerId: String
    ): SellerInfoResponse =
        sellerService.getSellerInfoById(UUID.fromString(sellerId))
}