package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.service

import com.github.f4b6a3.uuid.alt.GUID
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.entity.RefreshToken
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.repository.RefreshTokenRepository
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.JwtProperties
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository,
    jwtProperties: JwtProperties
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val refreshTokenValidityMs: Long = jwtProperties.refreshTokenValidityMs

    @Transactional
    fun createRefreshToken(authentication: Authentication): String {
        val uid = authentication.name

        val user = userRepository.findByUserEmail(uid)
            ?: throw DataNotFoundException("User not found with uid: $uid")

        val newToken: String = GUID.v7().toString()
        val expiryDate = LocalDateTime.now().plusSeconds(refreshTokenValidityMs / 1000)

        val existingToken = refreshTokenRepository.findByUserUserId(
            requireNotNull(user.userId) { "User ID in RefreshToken cannot be null" }
        )
        if (existingToken != null) {
            log.debug(
                "RefreshTokenService - updateRefreshToken: Updating existing refresh token for userId {}",
                user.userId
            )
            existingToken.updateToken(newToken, expiryDate)
        } else {
            log.debug("Creating new refresh token for userId {}", user.userId)

            val createdToken = refreshTokenRepository.save(
                RefreshToken(user, newToken, expiryDate)
            )
            log.debug("Created refresh token: {}", createdToken)
        }

        return newToken
    }

    @Transactional
    fun verifyRefreshToken(token: String): RefreshToken {
        val refreshToken = refreshTokenRepository.findByToken(token)
            ?: throw DataNotFoundException("Refresh token not found: $token")

        if (refreshToken.expiryDate.isBefore(LocalDateTime.now())) {
            log.debug("RefreshTokenService.verifyRefreshToken - Refresh token expired")
            refreshTokenRepository.delete(refreshToken)
            throw DataNotFoundException("Refresh token has expired: $token")
        }

        return refreshToken
    }

    @Transactional
    fun deleteRefreshToken(token: String) {
        refreshTokenRepository.deleteByToken(token)
    }
}
