package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto

import jakarta.validation.constraints.NotBlank

data class RefreshRequest(
    @field:NotBlank(message = "Refresh Token is Required")
    val refreshToken: String
)
