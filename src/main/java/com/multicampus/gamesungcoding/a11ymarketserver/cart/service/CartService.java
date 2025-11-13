package com.multicampus.gamesungcoding.a11ymarketserver.cart.service;


import com.multicampus.gamesungcoding.a11ymarketserver.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.dto.CartAddRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.dto.CartDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.entity.CartItems;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.repository.CartItemRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public List<CartDTO> getCartItems(UUID userId) {
        return cartItemRepository.findByCartId(GetCartIdByUserId(userId)).stream()
                .map(CartDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public int getCartTotal(UUID userId) {
        // 현재 cart_items에 price가 없으므로 총액은 이 단계에서 계산 불가.
        // TODO: product 가격 조인 후 합산 (ex. productRepository로 가격 조회해서 계산)
        return 0;
    }

    @Transactional
    public CartDTO addItem(CartAddRequest req) {
        var cartId = GetCartIdByUserId(req.getUserId());
        CartItems cart = cartItemRepository.findByCartIdAndProductId(cartId, req.getProductId())
                .map(existing -> {
                    existing.changeQuantity(existing.getQuantity() + req.getQuantity());
                    return existing;
                })
                .orElseGet(() -> CartItems.builder()
                        //.cartItemId(UUID.randomUUID())
                        .cartId(cartId)
                        .productId(req.getProductId())
                        .quantity(req.getQuantity())
                        .build()
                );
        return CartDTO.fromEntity(cartItemRepository.save(cart));
    }

    @Transactional
    public CartDTO updateQuantity(UUID cartItemId, int quantity) {

        CartItems cart = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found: " + cartItemId));

        cart.changeQuantity(quantity);
        return CartDTO.fromEntity(cartItemRepository.save(cart));
    }

    @Transactional
    public void deleteItems(List<UUID> itemIds) {
        cartItemRepository.deleteAllByIdInBatch(itemIds);
    }

    private UUID GetCartIdByUserId(UUID userId) {
        return cartRepository.findByUserId(userId)
                .map(Cart::getCartId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                                //.cartId(UUID.randomUUID())
                                .userId(userId)
                                .build())
                        .getCartId()
                );
    }
}