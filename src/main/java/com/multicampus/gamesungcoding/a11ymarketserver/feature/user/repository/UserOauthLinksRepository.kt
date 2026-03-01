package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserOauthLinks;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserOauthLinksRepository extends JpaRepository<UserOauthLinks, UUID> {

    @Query("SELECT l FROM UserOauthLinks l JOIN FETCH l.user WHERE l.oauthProviderId = :providerId")
    Optional<UserOauthLinks> findByOauthProviderId(String providerId);

    Optional<UserOauthLinks> findByUser(Users users);

    boolean existsByUser(Users user);
}
