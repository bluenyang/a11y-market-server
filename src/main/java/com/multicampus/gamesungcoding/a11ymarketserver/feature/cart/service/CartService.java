package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.service;


import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartItemRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    public CartItemListResponse getCartItems(String UserEmail) {
        var list = cartItemRepository.findAllByUserEmailToResponse(UserEmail);
        int total = list.stream()
                .mapToInt(item -> item.quantity() * item.productPrice())
                .sum();

        var groupedList = list.stream()
                .collect(Collectors.groupingBy(
                        CartItemDto::sellerName,
                        Collectors.mapping(
                                item ->
                                        item, Collectors.toList()
                        )
                ))
                .entrySet()
                .stream()
                .map(entry -> new CartItemListDto(
                        entry.getKey(),
                        entry.getValue().getFirst().sellerId(),
                        calculateItemsTotal(entry.getValue()),
                        entry.getValue()
                ))
                .toList();

        return new CartItemListResponse(groupedList, total);
    }

    @Transactional
    public CartItemUpdatedResponse addItem(CartAddRequest req, String userEmail) {
        var cartId = getCartIdByUserEmail(userEmail);
        CartItems cart = cartItemRepository.findByCartIdAndProductId(cartId, UUID.fromString(req.productId()))
                .map(existing -> {
                    existing.changeQuantity(existing.getQuantity() + req.quantity());
                    return existing;
                })
                .orElseGet(() -> CartItems.builder()
                        //.cartItemId(UUID.randomUUID())
                        .cartId(cartId)
                        .productId(UUID.fromString(req.productId()))
                        .quantity(req.quantity())
                        .build()
                );
        return CartItemUpdatedResponse.fromEntity(cartItemRepository.save(cart));
    }

    @Transactional
    public CartItemUpdatedResponse updateQuantity(UUID cartItemId, int quantity, String userEmail) {

        // 검증: 해당 cartItemId가 userEmail의 장바구니에 속하는지 확인
        UUID cartId = getCartIdByUserEmail(userEmail);
        CartItems existingItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found: " + cartItemId));
        if (!existingItem.getCartId().equals(cartId)) {
            throw new NoSuchElementException("Cart item does not belong to user: " + cartItemId);
        }

        CartItems cart = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found: " + cartItemId));

        cart.changeQuantity(quantity);
        return CartItemUpdatedResponse.fromEntity(cartItemRepository.save(cart));
    }

    @Transactional
    public void deleteItems(CartItemDeleteRequest itemIdsStr, String userEmail) {
        var itemIds = itemIdsStr
                .itemIds()
                .stream()
                .map(UUID::fromString)
                .toList();

        // 검증: 모든 itemIds가 userEmail의 장바구니에 속하는지 확인
        UUID cartId = getCartIdByUserEmail(userEmail);
        List<UUID> invalidItems = cartItemRepository.findAllById(itemIds).stream()
                .filter(item -> !item.getCartId().equals(cartId))
                .map(CartItems::getCartItemId)
                .toList();
        if (!invalidItems.isEmpty()) {
            throw new NoSuchElementException("Some cart items do not belong to user: " + invalidItems);
        }

        cartItemRepository.deleteAllByIdInBatch(itemIds);
    }

    private UUID getCartIdByUserEmail(String userEmail) {
        UUID userId = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userEmail))
                .getUserId();

        return cartRepository.findByUserId(userId)
                .map(Cart::getCartId)
                .orElseGet(() -> cartRepository
                        .save(Cart
                                .builder()
                                .userId(userId)
                                .build())
                        .getCartId()
                );
    }

    private int calculateItemsTotal(List<CartItemDto> items) {
        return items.stream()
                .mapToInt(item -> item.productPrice() * item.quantity())
                .sum();
    }
}