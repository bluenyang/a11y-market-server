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
    // yaml에서 값을 주입 받을 secret 필드
    @Getter
    private String secret;

    // 유효 시간 필드 (밀리초 단위)
    @Setter
    @Getter
    private long accessTokenValidityMs;

    // 유효 시간 필드 (밀리초 단위)
    @Setter
    @Getter
    private long refreshTokenValidityMs;

    // SecretKey 객체 필드
    @Getter
    private SecretKey secretKey;

    // secret 필드에 값이 주입될 때 SecretKey 객체도 함께 생성
    public void setSecret(String secret) {
        this.secret = secret;
        // Base64로 인코딩된 문자열을 디코딩하여 바이트 배열로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        // 바이트 배열을 사용하여 SecretKey 객체 생성
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }
}
