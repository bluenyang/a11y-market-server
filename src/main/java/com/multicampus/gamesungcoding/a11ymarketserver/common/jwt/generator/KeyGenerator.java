package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.generator;

import io.jsonwebtoken.Jwts;

import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        var key = Jwts.SIG.HS512.key().build();
        var base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Base64-encoded key: " + base64Key);
    }
}
