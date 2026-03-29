package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.service.CartService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
class CartController(
    private val cartService: CartService
) {
    // GET /api/v1/cart 목록 조회 기능
    @GetMapping("/v1/cart/me")
    fun getCart(
        @AuthenticationPrincipal userDetails: UserDetails
    ): CartItemListResponse = cartService.getCartItems(userDetails.username)

    @GetMapping("/v1/cart/me/items/count")
    fun getCartItemCount(
        @AuthenticationPrincipal userDetails: UserDetails
    ): CartItemCountResponse = cartService.getCartItemCount(userDetails.username)

    // POST /api/v1/cart/items 상품 추가 기능
    @PostMapping("/v1/cart/me/items")
    @ResponseStatus(HttpStatus.CREATED)
    fun addItem(
        @Valid @RequestBody req: @Valid CartAddRequest,
        @AuthenticationPrincipal userDetails: UserDetails,
        httpResponse: HttpServletResponse
    ): CartItemUpdatedResponse {
        httpResponse.setHeader(HttpHeaders.LOCATION, "/api/v1/cart/me")
        return cartService.addItem(req, userDetails.username)
    }

    // PATCH /api/v1/cart/items/{cartItemId} 수량 조정 기능
    @PatchMapping("/v1/cart/me/items/{cartItemId}")
    fun updateQuantity(
        @PathVariable @NotNull cartItemId: @NotNull String,
        @Valid @RequestBody body: @Valid CartQtyUpdateRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): CartItemUpdatedResponse = cartService.updateQuantity(
        UUID.fromString(cartItemId),
        body.quantity,
        userDetails.username
    )

    // DELETE /api/v1/cart/items 상품 삭제 기능
    @DeleteMapping("/v1/cart/me/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteItems(
        @Valid @RequestBody itemIds: @Valid CartItemDeleteRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ) {
        cartService.deleteItems(itemIds, userDetails.username)
    }
}