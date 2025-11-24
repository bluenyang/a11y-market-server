package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserInfo;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;

public record LoginResponse(UserInfo user,
                            String accessToken,
                            String refreshToken) {

    public static LoginResponse fromEntityAndTokens(Users user,
                                                    String accessToken,
                                                    String refreshToken) {

        return new LoginResponse(
                UserInfo.fromEntity(user),
                accessToken,
                refreshToken
        );
    }
}
