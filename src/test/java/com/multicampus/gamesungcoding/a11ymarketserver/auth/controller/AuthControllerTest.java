package com.multicampus.gamesungcoding.a11ymarketserver.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicampus.gamesungcoding.a11ymarketserver.common.config.SecurityConfig;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.service.RefreshTokenService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.controller.AuthController;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.JoinRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private RefreshTokenService refreshTokenService;
    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    private final String mockName = "User One";

    @Test
    @DisplayName("로그인 성공 테스트")
    void testLogin() throws Exception {
        String mockEmail = "user1@example.com";
        String mockPassword = "password123!";
        var mockReqDto = new LoginRequest(
                mockEmail,
                mockPassword);

        BDDMockito.given(this.authService.login(any()))
                .willReturn(new LoginResponse(
                        mockEmail,
                        this.mockName,
                        "USER",
                        "mockAccessToken",
                        "mockRefreshToken"));

        this.mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(mockReqDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃 성공 테스트")
    void testLogout() throws Exception {
        this.mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("SUCCESS"));
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void testJoin() throws Exception {
        var mockReqDto = new JoinRequest(
                "user1@example.com",
                "Password123!",
                this.mockName,
                this.mockName,
                "01012345678");

        BDDMockito.given(this.authService.join(any(JoinRequest.class)))
                .willReturn(
                        UserResponse.builder()
                                .userId(UUID.randomUUID())
                                .userEmail("user1@example.com")
                                .userName(this.mockName)
                                .build()
                );

        this.mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(mockReqDto))
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복 테스트")
    void testJoin_EmailConflict() throws Exception {
        var mockReqDto = new JoinRequest(
                "user1@example.com",
                "Password123!",
                this.mockName,
                this.mockName,
                "01012345678");

        BDDMockito.given(this.authService.join(any(JoinRequest.class)))
                .willThrow(new DataDuplicatedException("이미 존재하는 이메일입니다."));

        this.mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(mockReqDto))
                )
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 유효성 검사 테스트")
    void testJoin_InvalidPassword() throws Exception {
        var mockReqDto = new JoinRequest(
                "user1@example.com",
                "1234",
                this.mockName,
                this.mockName,
                "01012345678");

        BDDMockito.given(this.authService.join(any(JoinRequest.class)))
                .willReturn(null);

        this.mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(mockReqDto))
                )
                .andExpect(status().isBadRequest());
    }
}
