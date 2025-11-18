package com.multicampus.gamesungcoding.a11ymarketserver.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.JoinRequestDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        Users testUser = Users.builder()
                .userEmail("user1@example.com")
                .userName("User One")
                .userPass(this.passwordEncoder.encode("password123!"))
                .userNickname("user-one")
                .userPhone("01012345678")
                .userRole("USER")
                .build();
        this.userRepository.save(testUser);
    }

    @Test
    @WithMockUser
    @DisplayName("로그인 API 통합 테스트 - 성공 케이스")
    void testLoginIntegration() throws Exception {
        var loginReq = LoginDTO.builder()
                .email("user1@example.com")
                .password("password123!")
                .build();

        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 API 통합 테스트 - 실패 케이스")
    void testLoginIntegration_Failure() throws Exception {
        var loginReq = LoginDTO.builder()
                .email("user1@example.com")
                .password("wrongpassword!")
                .build();

        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user1@example.com", password = "password123!")
    @DisplayName("로그아웃 API 통합 테스트")
    void testLogoutIntegration() throws Exception {
        this.mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("로그아웃 성공"));
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 API 통합 테스트 - 성공 케이스")
    void testJoinIntegration() throws Exception {
        var joinReq = JoinRequestDTO.builder()
                .email("user2@example.com")
                .password("Password123!")
                .nickname("User Two")
                .username("User Two")
                .phone("01087654321")
                .build();

        this.mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.userEmail").value("user2@example.com"))
                .andExpect(jsonPath("$.user.userName").value("User Two"))
                .andExpect(jsonPath("$.msg").value("회원가입 성공"));
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 API 통합 테스트 - 이메일 중복 케이스")
    void testJoinIntegration_DuplicateEmail() throws Exception {
        var joinReq = JoinRequestDTO.builder()
                .email("user1@example.com") // 이미 존재하는 이메일
                .password("Password123!")
                .nickname("User One Duplicate")
                .username("User One Duplicate")
                .phone("01099998888")
                .build();

        this.mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinReq)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."));
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 API 통합 테스트 - 유효성 검사 실패 케이스")
    void testJoinIntegration_ValidationFailure() throws Exception {
        var joinReq = JoinRequestDTO.builder()
                .email("invalid-email-format") // 잘못된 이메일 형식
                .password("short") // 너무 짧은 비밀번호
                .nickname("") // 빈 닉네임
                .username("User Invalid")
                .phone("01012345678")
                .build();

        this.mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinReq)))
                .andExpect(status().isBadRequest());
    }
}
