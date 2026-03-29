package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CartRepository : JpaRepository<Cart, UUID> {
    fun findByUser(user: Users): Cart?

    fun findByUserUserId(userId: UUID): Cart?
}
