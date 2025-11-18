package com.multicampus.gamesungcoding.a11ymarketserver.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.controller.AuthController;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.JoinRequestDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.UserRespDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.common.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID mockUserId = UUID.randomUUID();
    private final String mockName = "User One";

    @Test
    @DisplayName("로그인 성공 테스트")
    void testLogin() throws Exception {
        String mockEmail = "user1@example.com";
        String mockPassword = "password123!";
        var mockReqDto = LoginDTO.builder()
                .email(mockEmail)
                .password(mockPassword)
                .build();
        var mockResp = UserRespDTO.builder()
                .userId(this.mockUserId)
                .userEmail(mockEmail)
                .userName(this.mockName)
                .build();

        BDDMockito.given(this.authService.login(any(LoginDTO.class)))
                .willReturn(mockResp);

        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(mockReqDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(this.mockUserId.toString()))
                .andExpect(jsonPath("$.userEmail").value(mockEmail))
                .andExpect(jsonPath("$.userName").value(this.mockName));
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    void testLogout() throws Exception {
        this.mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("로그아웃 성공"));
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void testJoin() throws Exception {
        var mockReqDto = JoinRequestDTO.builder()
                .email("user1@example.com")
                .password("Password123!")
                .nickname(this.mockName)
                .username(this.mockName)
                .phone("01012345678")
                .build();

        BDDMockito.given(this.authService.join(any(JoinRequestDTO.class)))
                .willReturn(UserRespDTO.builder()
                        .userId(UUID.randomUUID())
                        .userEmail("user1@example.com")
                        .userName(this.mockName)
                        .build());

        this.mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(mockReqDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("회원가입 성공"));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복 테스트")
    void testJoin_EmailConflict() throws Exception {
        var mockReqDto = JoinRequestDTO.builder()
                .email("user1@example.com")
                .password("Password123!")
                .nickname(this.mockName)
                .username(this.mockName)
                .phone("01012345678")
                .build();

        BDDMockito.given(this.authService.join(any(JoinRequestDTO.class)))
                .willReturn(null);

        this.mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(mockReqDto))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 유효성 검사 테스트")
    void testJoin_InvalidPassword() throws Exception {
        var mockReqDto = JoinRequestDTO.builder()
                .email("user1@example.com")
                .password("1234")
                .nickname(this.mockName)
                .username(this.mockName)
                .phone("01012345678")
                .build();

        BDDMockito.given(this.authService.join(any(JoinRequestDTO.class)))
                .willReturn(null);

        this.mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(mockReqDto))
                )
                .andExpect(status().isBadRequest());
    }
}
