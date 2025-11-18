package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.controller;


import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartAddRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemsResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartQtyUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class CartController {

    private final CartService cartService;

    // GET /api/v1/cart 목록 조회 기능
    @GetMapping("/v1/cart/me")
    public ResponseEntity<CartItemsResponse> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<CartDTO> items = cartService.getCartItems(userDetails.getUsername());
        int total = cartService.getCartTotal(userDetails.getUsername());

        CartItemsResponse body = CartItemsResponse.builder()
                .items(items)
                .total(total)
                .build();

        return ResponseEntity.ok(body);
    }

    // POST /api/v1/cart/items 상품 추가 기능
    @PostMapping("/v1/cart/items")
    public ResponseEntity<CartDTO> addItem(
            @Valid @RequestBody CartAddRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {

        CartDTO created = cartService.addItem(req, userDetails.getUsername());
        return ResponseEntity
                .created(URI.create("/api/v1/cart/items/" + created.getCartItemId()))
                .body(created);
    }

    // PATCH /api/v1/cart/items/{cartItemId} 수량 조정 기능
    @PatchMapping("/v1/cart/items/{cartItemId}")
    public ResponseEntity<CartDTO> updateQuantity(
            @PathVariable @NotNull String cartItemId,      // [추가] path 변수 null 불가
            @Valid @RequestBody CartQtyUpdateRequest body,
            @AuthenticationPrincipal UserDetails userDetails) {
        CartDTO updated = cartService.updateQuantity(
                UUID.fromString(cartItemId),
                body.getQuantity(),
                userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/v1/cart/items?itemIds=1,2,3 삭제 기능
    @DeleteMapping("/v1/cart/items")
    public ResponseEntity<Void> deleteItems(
            @RequestBody @NotEmpty List<@NotNull String> itemIds,
            @AuthenticationPrincipal UserDetails userDetails) {
        cartService.deleteItems(itemIds, userDetails.getUsername());
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}