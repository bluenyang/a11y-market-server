package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class JwtResponse {
    String accessToken;
    String refreshToken;
}
