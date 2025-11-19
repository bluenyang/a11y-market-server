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

    // Initialize secretKey and accessTokenValidityMs from JwtProperties with dependency injection
    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.secretKey = jwtProperties.getSecretKey();
        this.accessTokenValidityMs = jwtProperties.getAccessTokenValidityMs();
    }

    /**
     * Create Access Token
     *
     * @param authentication the authentication object containing user details
     * @return the generated JWT access token
     */
    public String createAccessToken(Authentication authentication) {

        // e.g., "ROLE_USER,ROLE_ADMIN"
        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Token issue and expiry dates
        var now = new Date();
        var expiryDate = new Date(now.getTime() + accessTokenValidityMs);

        return Jwts.builder()
                .subject(authentication.getName()) // typically the username
                .claim("auth", authorities)     // auth information
                .issuedAt(now)                     // token issue time
                .expiration(expiryDate)            // token expiry time
                .signWith(secretKey)               // sign the token with the secret key
                .compact();
    }

    /**
     * Get Authentication from Token
     *
     * @param token the JWT token
     * @return the Authentication object
     */
    public Authentication getAuthentication(String token) {
        // Get Claims from the token with secret key verification
        var claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Convert String from "auth" claim to Collection<? extends GrantedAuthority>
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        // Create UserDetails object based on subject and authorities
        var principal = new User(claims.getSubject(), "", authorities);

        // Create and return Authentication object
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    /**
     * Validate Token
     *
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            // try to parse the token. If parsing fails, an exception will be thrown.
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token format: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (io.jsonwebtoken.security.SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT token validation error: {}", e.getMessage());
        }
        return false;
    }
}
