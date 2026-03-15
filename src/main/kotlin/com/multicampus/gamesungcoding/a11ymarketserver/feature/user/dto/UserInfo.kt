package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole

data class UserInfo(
    val userEmail: String,
    val userNickname: String,
    val userRole: UserRole
)
