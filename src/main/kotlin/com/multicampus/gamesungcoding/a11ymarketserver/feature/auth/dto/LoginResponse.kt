package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserInfo
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.mapper.toInfo

data class LoginResponse(
    val user: UserInfo,
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun fromEntityAndTokens(user: Users, accessToken: String, refreshToken: String) =
            LoginResponse(
                user = user.toInfo(),
                accessToken = accessToken,
                refreshToken = refreshToken
            )
    }
}
