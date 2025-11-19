package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.entity.RefreshToken;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.repository.RefreshTokenRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.JwtProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
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

        var user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("User not found with email: " + userEmail));
        var userId = user.getUserId();

        var existingTokenOpt = refreshTokenRepository.findByUserId(userId);
        var newToken = UUID.randomUUID().toString();

        var expiryDate = LocalDateTime.now().plusSeconds(refreshTokenValidityMs / 1000);

        if (existingTokenOpt.isPresent()) {
            var existingToken = existingTokenOpt.get();
            existingToken.updateToken(newToken, expiryDate);
        } else {
            var refreshToken = RefreshToken.builder()
                    .userId(userId)
                    .token(newToken)
                    .expiryDate(expiryDate)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }

        return newToken;
    }

    @Transactional
    public RefreshToken verifyRefreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new DataNotFoundException("Refresh token not found: " + token));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
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
