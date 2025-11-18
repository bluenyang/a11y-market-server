package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItems, UUID> {

    Optional<CartItems> findByCartIdAndProductId(UUID userId, UUID productId);

    Collection<CartItems> findByCartId(UUID cartId);
}