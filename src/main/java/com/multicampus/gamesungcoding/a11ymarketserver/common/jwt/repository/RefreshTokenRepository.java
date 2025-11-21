package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(UUID userId);

    void deleteByToken(String token);
}
