package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserId(UUID userId);

    @Query("""
            
            SELECT c
            FROM Cart c
            WHERE c.userId = (
                SELECT u.userId
                FROM Users u
                WHERE u.userEmail = :email
            )
            """)
    Optional<Cart> findCartByUserEmail(@Param("email") String userEmail);
}
