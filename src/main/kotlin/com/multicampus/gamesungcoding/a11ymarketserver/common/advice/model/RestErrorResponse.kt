package com.multicampus.gamesungcoding.a11ymarketserver.common.advice.model

import java.time.LocalDateTime

data class RestErrorResponse(
    val status: Int,
    val error: ErrorRespStatus,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
