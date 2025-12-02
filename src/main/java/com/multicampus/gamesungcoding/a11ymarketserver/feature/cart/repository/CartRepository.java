package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser(Users user);
}
