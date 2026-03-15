package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserA11yProfileResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserA11yProfile

fun UserA11yProfile.toA11yProfileResponse(): UserA11yProfileResponse {
    val pi = this.profileInfo
    return UserA11yProfileResponse(
        profileId = requireNotNull(this.profileId) { "Profile ID cannot be null" },
        profileName = pi.profileName,
        description = pi.description,
        contrastLevel = pi.contrastLevel,
        textSizeLevel = pi.textSizeLevel,
        textSpacingLevel = pi.textSpacingLevel,
        lineHeightLevel = pi.lineHeightLevel,
        textAlign = pi.textAlign,
        screenReader = pi.screenReader,
        smartContrast = pi.smartContrast,
        highlightLinks = pi.highlightLinks,
        cursorHighlight = pi.cursorHighlight,
        createdAt = this.createdAt ?: throw InvalidRequestException("CreatedAt cannot be null"),
        updatedAt = this.updatedAt ?: throw InvalidRequestException("UpdatedAt cannot be null"),
    )
}