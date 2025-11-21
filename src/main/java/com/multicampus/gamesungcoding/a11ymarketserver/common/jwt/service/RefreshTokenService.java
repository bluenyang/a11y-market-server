package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.entity.RefreshToken;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.repository.RefreshTokenRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.JwtProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    // from JwtProperties
    private final long refreshTokenValidityMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserRepository userRepository,
                               JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.refreshTokenValidityMs = jwtProperties.getRefreshTokenValidityMs();
    }

    @Transactional
    public String createRefreshToken(Authentication authentication) {
        String userEmail = authentication.getName();

        var userId = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("User not found with email: " + userEmail))
                .getUserId();

        var newToken = UUID.randomUUID().toString();
        var expiryDate = LocalDateTime.now().plusSeconds(refreshTokenValidityMs / 1000);

        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        token -> {
                            log.debug("RefreshTokenService - updateRefreshToken: Updating existing refresh token for userId {}", userId);
                            token.updateToken(newToken, expiryDate);
                        },
                        () -> {
                            log.debug("RefreshTokenService - updateRefreshToken: Creating new refresh token for userId {}", userId);
                            var createdToken = refreshTokenRepository.save(RefreshToken.builder()
                                    .userId(userId)
                                    .token(newToken)
                                    .expiryDate(expiryDate)
                                    .build());
                            log.debug("RefreshTokenService - updateRefreshToken: Created refresh token: {}", createdToken);
                        }
                );
        return newToken;
    }

    @Transactional
    public RefreshToken verifyRefreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new DataNotFoundException("Refresh token not found: " + token));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.debug("RefreshTokenService.verifyRefreshToken - Refresh token expired");
            refreshTokenRepository.delete(refreshToken);
            throw new DataNotFoundException("Refresh token has expired: " + token);
        } else {
            return refreshToken;
        }
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

}
