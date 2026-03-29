package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CartItemRepository : JpaRepository<CartItems, UUID> {
    fun findByCartAndProductProductId(cart: Cart, productId: UUID): CartItems?

    fun findAllByCart(cart: Cart): List<CartItems>

    @Query(
        """
            SELECT DISTINCT ci FROM CartItems ci
            JOIN FETCH ci.product p
            LEFT JOIN FETCH p.productImages pi
            WHERE ci.cartItemId IN :cartItemIds
            
            """
    )
    fun findAllByIdWithProductAndImage(
        // Kotlin에서는 명시적으로 @Param 어노테이션을 사용하여 매개변수 이름을 지정
        @Param("cartItemIds") cartItemIds: List<UUID>
    ): List<CartItems>

    fun countByCart(cartByUserEmail: Cart): Int
}