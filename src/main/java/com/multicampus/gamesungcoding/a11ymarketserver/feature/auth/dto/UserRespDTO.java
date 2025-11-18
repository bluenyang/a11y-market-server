package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRespDTO {
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userNickname;
    private String userRole;

    public static UserRespDTO fromEntity(Users user) {
        return UserRespDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userPhone(user.getUserPhone())
                .userNickname(user.getUserNickname())
                .userRole(user.getUserRole())
                .build();
    }
}
