package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model;

public record UserInfo(String userEmail, String userNickname, String userRole) {
    public static UserInfo fromEntity(Users user) {
        return new UserInfo(
                user.getUserEmail(),
                user.getUserNickname(),
                user.getUserRole()
        );
    }
}
