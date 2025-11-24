package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemDto;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItems, UUID> {

    Optional<CartItems> findByCartIdAndProductId(UUID userId, UUID productId);

    @Query("""
            SELECT ci
            FROM CartItems ci
            WHERE ci.cartId = (
                SELECT c.cartId
                FROM Cart c
                WHERE c.userId = (
                    SELECT u.userId
                    FROM Users u
                    WHERE u.userEmail = :email
                )
            )
            """)
    List<CartItems> findByUserEmail(@Param("email") String userEmail);

    @Query("""
            SELECT new com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemDto(
                ci.cartItemId,
                ci.cartId,
                ci.productId,
                s.sellerId,
                s.sellerName,
                p.productName,
                p.productPrice,
                cat.categoryName,
                ci.quantity
            )
            FROM CartItems ci
                        JOIN Product p ON ci.productId = p.productId
                        JOIN Categories cat ON p.categoryId = cat.categoryId
                        JOIN Seller s ON p.sellerId = s.sellerId
            WHERE ci.cartId = (
                SELECT c.cartId
                FROM Cart c
                WHERE c.userId = (
                    SELECT u.userId
                    FROM Users u
                    WHERE u.userEmail = :email
                )
            )
            """)
    List<CartItemDto> findAllByUserEmailToResponse(@Param("email") String userEmail);
}