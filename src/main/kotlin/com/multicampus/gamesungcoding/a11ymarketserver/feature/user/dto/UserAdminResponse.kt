package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole
import java.time.LocalDateTime
import java.util.*

data class UserAdminResponse(
    val userId: UUID,
    val userName: String,
    val userEmail: String,
    val userNickname: String,
    val userRole: UserRole,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
