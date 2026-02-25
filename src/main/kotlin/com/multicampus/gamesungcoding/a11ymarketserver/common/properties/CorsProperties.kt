package com.multicampus.gamesungcoding.a11ymarketserver.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.Collections.emptyList

@ConfigurationProperties(prefix = "cors")
data class CorsProperties(
    val allowedOriginPatterns: MutableList<String> = emptyList()
)
