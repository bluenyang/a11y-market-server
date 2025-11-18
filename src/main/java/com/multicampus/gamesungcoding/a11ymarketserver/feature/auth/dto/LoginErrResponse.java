package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginErrResponse {
    String message;
}
