package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.util.*

data class ImageMetadata(
    @field:NotBlank
    val originalFileName: String,

    val altText: String,

    @field:Min(0)
    val sequence: Int,

    val imageId: UUID,

    val isNew: Boolean
)
