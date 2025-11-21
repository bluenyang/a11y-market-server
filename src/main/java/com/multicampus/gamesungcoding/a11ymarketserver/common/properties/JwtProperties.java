package com.multicampus.gamesungcoding.a11ymarketserver.common.properties;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @Getter
    private String secret;

    @Setter
    @Getter
    private long accessTokenValidityMs;

    @Setter
    @Getter
    private long refreshTokenValidityMs;

    @Getter
    private SecretKey secretKey;

    public void setSecret(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }
}
