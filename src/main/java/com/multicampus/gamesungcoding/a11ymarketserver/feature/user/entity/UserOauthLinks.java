package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "user_oauth_links")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOauthLinks {
    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    private UUID userOauthLinkId;

    @JoinColumn(name = "user_id", unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @Column(length = 50)
    private String oauthProvider;

    @Column
    private String oauthProviderId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private UserOauthLinks(Users user,
                           String oauthProvider,
                           String oauthProviderId) {
        this.user = user;
        this.oauthProvider = oauthProvider;
        this.oauthProviderId = oauthProviderId;
    }

    public void updateUser(Users user) {
        this.user = user;
    }
}
