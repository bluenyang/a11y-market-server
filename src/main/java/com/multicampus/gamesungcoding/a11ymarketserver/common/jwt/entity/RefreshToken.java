package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    private UUID refreshTokenId;

    @Column(length = 16, nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    public RefreshToken(UUID userId, String token, LocalDateTime expiryDate) {
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public void updateToken(String token, LocalDateTime expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
