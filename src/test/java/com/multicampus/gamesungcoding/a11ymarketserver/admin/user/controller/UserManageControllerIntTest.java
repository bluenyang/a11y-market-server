package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.controller;

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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class UserManageControllerIntTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UUID testUserId1;

    @BeforeEach
    void setUp() {
        Users testUser1 = Users.builder()
                .userEmail("user1@example.com")
                .userName("User One")
                .userPass(this.passwordEncoder.encode("password123!"))
                .userNickname("user-one")
                .userPhone("01012345678")
                .userRole("USER")
                .build();
        Users testUser2 = Users.builder()
                .userEmail("user2@example.com")
                .userName("User Two")
                .userPass(this.passwordEncoder.encode("password456!"))
                .userNickname("user-two")
                .userPhone("01087654321")
                .userRole("USER")
                .build();
        this.userRepository.save(testUser1);
        this.userRepository.save(testUser2);
        this.testUserId1 = testUser1.getUserId();
    }

    @Test
    @WithMockUser
    @DisplayName("사용자 관리 API 통합 테스트 - 사용자 목록 조회")
    void testGetUserList() throws Exception {
        this.mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser
    @DisplayName("사용자 관리 API 통합 테스트 - 특정 사용자 권한 변경")
    void testChangeUserRole() throws Exception {
        // 변경 시도
        this.mockMvc.perform(patch("/api/v1/admin/users/{userId}/permission", testUserId1)
                        .contentType(MediaType.APPLICATION_JSON)
                        // ADMIN 으로 변경
                        .param("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userRole").value("ADMIN"));

        // 변경 확인
        var updatedUser = userRepository.findById(testUserId1).orElseThrow();
        // assertj 사용
        assertThat(updatedUser.getUserRole().equals("ADMIN"));
    }
}
