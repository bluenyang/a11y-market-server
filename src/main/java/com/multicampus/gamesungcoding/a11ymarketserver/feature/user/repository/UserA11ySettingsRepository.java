package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11ySettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserA11ySettingsRepository extends JpaRepository<UserA11ySettings, UUID> {

    Optional<UserA11ySettings> findByUserId(UUID userId);
}
