package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto

@JvmRecord
data class JwtResponse(val accessToken: String, val refreshToken: String)
