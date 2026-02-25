package com.multicampus.gamesungcoding.a11ymarketserver.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "toss.payment")
data class TossPaymentProperties(
    val secretKey: String
)
