package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserAdminResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserInfo
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users

fun Users.toResponse(): UserResponse =
    UserResponse(
        userId = requireNotNull(this.userId) { "This entity is not stored in Database" },
        userName = this.userName,
        userEmail = this.userEmail,
        userPhone = this.userPhone ?: "",
        userNickname = this.userNickname,
        createdAt = this.createdAt ?: throw IllegalStateException("Created at is null"),
        updatedAt = this.updatedAt ?: throw IllegalStateException("Updated at is null"),
        userRole = this.userRole,
        sellerSubmitStatus = this.seller?.sellerSubmitStatus
    )

fun Users.toInfo(): UserInfo =
    UserInfo(
        userEmail = this.userEmail,
        userNickname = this.userNickname,
        userRole = this.userRole
    )

fun Users.toAdminResponse(): UserAdminResponse {
    // 이름을 홍*동 형태로 마스킹
    val name = this.userName
    val maskedName = when (name.length) {
        0, 1 -> name
        2 -> "${name[0]}*"
        else -> "${name[0]}${"*".repeat(name.length - 2)}${name.last()}"
    }

    return UserAdminResponse(
        userId = requireNotNull(this.userId) { "This entity is not stored in Database" },
        userName = maskedName,
        userEmail = this.userEmail,
        userNickname = this.userNickname,
        userRole = this.userRole,
        createdAt = this.createdAt ?: throw IllegalStateException("Created at is null"),
        updatedAt = this.updatedAt ?: throw IllegalStateException("Updated at is null"),
    )
}