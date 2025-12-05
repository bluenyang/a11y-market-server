package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.service;


import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartItemRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartItemListResponse getCartItems(String UserEmail) {
        // var list = cartItemRepository.findAllByUserEmailToResponse(UserEmail);
        Cart cart = getCartByUserEmail(UserEmail);
        var list = cartItemRepository.findAllByCart(cart).stream()
                .map(CartItemDto::fromEntity)
                .toList();
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
        var cartId = getCartByUserEmail(userEmail);
        var productProxy = productRepository.getReferenceById(UUID.fromString(req.productId()));

        CartItems cart = cartItemRepository.findByCartAndProduct_ProductId(cartId, UUID.fromString(req.productId()))
                .map(existing -> {
                    existing.changeQuantity(existing.getQuantity() + req.quantity());
                    return existing;
                })
                .orElseGet(() -> CartItems.builder()
                        //.cartItemId(UUID.randomUUID())
                        .cart(cartId)
                        .product(productProxy)
                        .quantity(req.quantity())
                        .build()
                );
        return CartItemUpdatedResponse.fromEntity(cartItemRepository.save(cart));
    }

    @Transactional
    public CartItemUpdatedResponse updateQuantity(UUID cartItemId, int quantity, String userEmail) {

        // 검증: 해당 cartItemId가 userEmail의 장바구니에 속하는지 확인
        Cart cart = getCartByUserEmail(userEmail);
        CartItems existingItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found: " + cartItemId));
        if (!existingItem
                .getCart()
                .getCartId()
                .equals(cart.getCartId())) {

            throw new DataNotFoundException("Cart item does not belong to user: " + cartItemId);
        }

        CartItems cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found: " + cartItemId));

        cartItem.changeQuantity(quantity);

        return CartItemUpdatedResponse.fromEntity(
                cartItemRepository.save(cartItem));
    }

    @Transactional
    public void deleteItems(CartItemDeleteRequest itemIdsStr, String userEmail) {
        var itemIds = itemIdsStr
                .itemIds()
                .stream()
                .map(UUID::fromString)
                .toList();

        // 검증: 모든 itemIds가 userEmail의 장바구니에 속하는지 확인
        var cart = getCartByUserEmail(userEmail);
        List<UUID> invalidItems = cartItemRepository.findAllById(itemIds).stream()
                .filter(item -> !item.getCart()
                        .getCartId()
                        .equals(cart.getCartId()))
                .map(CartItems::getCartItemId)
                .toList();
        if (!invalidItems.isEmpty()) {
            throw new DataNotFoundException("Some cart items do not belong to user: " + invalidItems);
        }

        cartItemRepository.deleteAllByIdInBatch(itemIds);
    }

    private Cart getCartByUserEmail(String userEmail) {
        Users user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                            log.debug("장바구니가 없어 새로 생성합니다. userEmail={}", userEmail);
                            return cartRepository.save(
                                    Cart.builder()
                                            .user(user)
                                            .build());
                        }
                );
    }

    private int calculateItemsTotal(List<CartItemDto> items) {
        return items.stream()
                .mapToInt(item -> item.productPrice() * item.quantity())
                .sum();
    }

    @Transactional(readOnly = true)
    public CartItemCountResponse getCartItemCount(String userEmail) {
        return new CartItemCountResponse(
                cartItemRepository.countByCart(
                        getCartByUserEmail(userEmail)
                )
        );
    }
}