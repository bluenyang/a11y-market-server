package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.generator

import io.jsonwebtoken.Jwts
import java.util.*

object KeyGenerator {
    @JvmStatic
    fun main(args: Array<String>) {
        val key = Jwts.SIG.HS512.key().build()
        val base64Key = Base64.getEncoder().encodeToString(key.encoded)
        println("Generated Base64-encoded key: $base64Key")
    }
}
