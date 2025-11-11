package com.multicampus.gamesungcoding.a11ymarketserver.cart.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserId(UUID userId);
}
