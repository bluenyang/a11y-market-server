package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItems, UUID> {

    Optional<CartItems> findByCartAndProduct_ProductId(Cart cart, UUID productId);

    List<CartItems> findByCart_User_UserEmail(String userEmail);

    List<CartItems> findAllByCart(Cart cart);

    @Query("""
            SELECT DISTINCT ci FROM CartItems ci
            JOIN FETCH ci.product p
            LEFT JOIN FETCH p.productImages pi
            WHERE ci.cartItemId IN :cartItemIds
            """)
    List<CartItems> findAllByIdWithProductAndImage(List<UUID> cartItemIds);

    Integer countByCart(Cart cartByUserEmail);
}