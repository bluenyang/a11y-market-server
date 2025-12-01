package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemDto;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItems, UUID> {

    Optional<CartItems> findByCartAndProduct_ProductId(Cart cart, UUID productId);

    List<CartItems> findByCart_User_UserEmail(String userEmail);

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
                ci.quantity,
                pi.imageUrl
            )
            FROM CartItems ci
                        JOIN Product p ON ci.productId = p.productId
                        JOIN Categories cat ON p.categoryId = cat.categoryId
                        JOIN Seller s ON p.sellerId = s.sellerId
                        LEFT JOIN ProductImages pi ON p.productId = pi.productId
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

    List<CartItems> findAllByCart_User_UserEmail(String userEmail);
}