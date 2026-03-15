package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto

import java.time.LocalDateTime
import java.util.*

data class UserA11yProfileResponse(
    val profileId: UUID,
    val profileName: String,
    val description: String,
    val contrastLevel: Int,
    val textSizeLevel: Int,
    val textSpacingLevel: Int,
    val lineHeightLevel: Int,
    val textAlign: String,
    val screenReader: Boolean,
    val smartContrast: Boolean,
    val highlightLinks: Boolean,
    val cursorHighlight: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
