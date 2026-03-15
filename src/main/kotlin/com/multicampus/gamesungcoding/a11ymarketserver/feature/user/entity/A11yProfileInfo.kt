package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class A11yProfileInfo(
    @Column(nullable = false, length = 50)
    val profileName: String,

    @Column(length = 200)
    val description: String,

    @Column(nullable = false)
    val contrastLevel: Int,

    @Column(nullable = false)
    val textSizeLevel: Int,

    @Column(nullable = false)
    val textSpacingLevel: Int,

    @Column(nullable = false)
    val lineHeightLevel: Int,

    @Column(length = 10, nullable = false)
    val textAlign: String,

    @Column(nullable = false)
    val screenReader: Boolean,

    @Column(nullable = false)
    val smartContrast: Boolean,

    @Column(nullable = false)
    val highlightLinks: Boolean,

    @Column(nullable = false)
    val cursorHighlight: Boolean,
)
