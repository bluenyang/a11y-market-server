package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserResponse {
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userNickname;
    private String userRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse fromEntity(Users user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userPhone(user.getUserPhone())
                .userNickname(user.getUserNickname())
                .userRole(user.getUserRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
