package com.multicampus.gamesungcoding.a11ymarketserver.cart.controller;


import com.multicampus.gamesungcoding.a11ymarketserver.cart.dto.CartAddRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.dto.CartDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.dto.CartItemsResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.dto.CartQtyUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CartItemsResponse> getCart(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");

        List<CartDTO> items = cartService.getCartItems(userId);
        int total = cartService.getCartTotal(userId);

        CartItemsResponse body = CartItemsResponse.builder()
                .items(items)
                .total(total)
                .build();

        return ResponseEntity.ok(body);
    }

    // POST /api/v1/cart/items 상품 검색 기능
    @PostMapping("/v1/cart/items")
    public ResponseEntity<CartDTO> addItem(@Valid @RequestBody CartAddRequest req) {
        CartDTO created = cartService.addItem(req);
        return ResponseEntity
                .created(URI.create("/api/v1/cart/items/" + created.getCartItemId()))
                .body(created);
    }

    // PATCH /api/v1/cart/items/{cartItemId} 수량 조정 기능
    @PatchMapping("/v1/cart/items/{cartItemId}")
    public ResponseEntity<CartDTO> updateQuantity(
            @PathVariable @NotNull UUID cartItemId,      // [추가] path 변수 null 불가
            @Valid @RequestBody CartQtyUpdateRequest body
    ) {
        CartDTO updated = cartService.updateQuantity(cartItemId, body.getQuantity());
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/v1/cart/items?itemIds=1,2,3 삭제 기능
    @DeleteMapping("/v1/cart/items")
    public ResponseEntity<Void> deleteItems(
            @RequestParam("itemIds") @NotEmpty List<@NotNull UUID> itemIds
    ) {
        cartService.deleteItems(itemIds);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}