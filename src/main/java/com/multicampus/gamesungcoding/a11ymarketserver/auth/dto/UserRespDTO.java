package com.multicampus.gamesungcoding.a11ymarketserver.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserRespDTO {
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userNickname;
    private String userRole;
}
