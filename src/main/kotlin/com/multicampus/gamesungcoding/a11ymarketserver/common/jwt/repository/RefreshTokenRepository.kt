package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.repository

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
    fun findByToken(token: String): RefreshToken?

    fun findByUserUserId(userId: UUID): RefreshToken?

    fun deleteByToken(token: String)
}
