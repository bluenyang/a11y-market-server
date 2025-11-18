package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.service;


import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartAddRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartDTO;
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

    public List<CartDTO> getCartItems(String UserEmail) {
        return cartItemRepository.findByCartId(GetCartIdByUserEmail(UserEmail))
                .stream()
                .map(CartDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public int getCartTotal(String userEmail) {
        // 현재 cart_items에 price가 없으므로 총액은 이 단계에서 계산 불가.
        // TODO: product 가격 조인 후 합산 (ex. productRepository로 가격 조회해서 계산)
        return 0;
    }

    @Transactional
    public CartDTO addItem(CartAddRequest req, String userEmail) {
        var cartId = GetCartIdByUserEmail(userEmail);
        CartItems cart = cartItemRepository.findByCartIdAndProductId(cartId, UUID.fromString(req.getProductId()))
                .map(existing -> {
                    existing.changeQuantity(existing.getQuantity() + req.getQuantity());
                    return existing;
                })
                .orElseGet(() -> CartItems.builder()
                        //.cartItemId(UUID.randomUUID())
                        .cartId(cartId)
                        .productId(UUID.fromString(req.getProductId()))
                        .quantity(req.getQuantity())
                        .build()
                );
        return CartDTO.fromEntity(cartItemRepository.save(cart));
    }

    @Transactional
    public CartDTO updateQuantity(UUID cartItemId, int quantity, String userEmail) {

        // 검증: 해당 cartItemId가 userEmail의 장바구니에 속하는지 확인
        UUID cartId = GetCartIdByUserEmail(userEmail);
        CartItems existingItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found: " + cartItemId));
        if (!existingItem.getCartId().equals(cartId)) {
            throw new NoSuchElementException("Cart item does not belong to user: " + cartItemId);
        }

        CartItems cart = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found: " + cartItemId));

        cart.changeQuantity(quantity);
        return CartDTO.fromEntity(cartItemRepository.save(cart));
    }

    @Transactional
    public void deleteItems(List<String> itemIdsStr, String userEmail) {
        var itemIds = itemIdsStr.stream()
                .map(UUID::fromString)
                .toList();

        // 검증: 모든 itemIds가 userEmail의 장바구니에 속하는지 확인
        UUID cartId = GetCartIdByUserEmail(userEmail);
        List<UUID> invalidItems = cartItemRepository.findAllById(itemIds).stream()
                .filter(item -> !item.getCartId().equals(cartId))
                .map(CartItems::getCartItemId)
                .toList();
        if (!invalidItems.isEmpty()) {
            throw new NoSuchElementException("Some cart items do not belong to user: " + invalidItems);
        }

        cartItemRepository.deleteAllByIdInBatch(itemIds);
    }

    private UUID GetCartIdByUserEmail(String userEmail) {
        UUID userId = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userEmail))
                .getUserId();

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