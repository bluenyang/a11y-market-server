package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto

import jakarta.validation.constraints.NotBlank

@JvmRecord
data class RefreshRequest(
    @field:NotBlank("Refresh Token is Required")
    val refreshToken: String?
)
