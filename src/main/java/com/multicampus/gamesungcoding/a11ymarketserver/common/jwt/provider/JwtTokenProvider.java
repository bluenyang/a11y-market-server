package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider;

import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long accessTokenValidityMs;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.secretKey = jwtProperties.getSecretKey();
        this.accessTokenValidityMs = jwtProperties.getAccessTokenValidityMs();
    }

    public String createAccessToken(Authentication authentication) {
        // e.g., "ROLE_USER,ROLE_ADMIN"
        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        var now = new Date();
        var expiryDate = new Date(now.getTime() + accessTokenValidityMs);

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        var claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        var principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            log.debug("JwtTokenProvider - validateToken");
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.debug("JwtTokenProvider.getAuthentication - Invalid JWT token format: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.debug("JwtTokenProvider.getAuthentication - Expired JWT token: {}", e.getMessage());
        } catch (SecurityException e) {
            log.debug("JwtTokenProvider.getAuthentication - Invalid JWT signature: {}", e.getMessage());
        } catch (Exception e) {
            log.debug("JwtTokenProvider.getAuthentication - JWT token validation error: {}", e.getMessage());
        }
        return false;
    }
}
