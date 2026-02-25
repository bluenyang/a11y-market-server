package com.multicampus.gamesungcoding.a11ymarketserver.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth2")
data class OAuth2Properties(
    val redirectUri: String,
    val signupUri: String
)
