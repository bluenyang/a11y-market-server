package com.multicampus.gamesungcoding.a11ymarketserver.auth.service;

import com.multicampus.gamesungcoding.a11ymarketserver.auth.dto.LoginDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceIntegrationTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성 및 저장
        this.userRepository.save(Users.builder()
                .userEmail("user1@example.com")
                .userPass(this.passwordEncoder.encode("password123!"))
                .userName("User One")
                .userNickname("user-one")
                .userPhone("01012345678")
                .userRole("USER")
                .build());
    }

    @Test
    @DisplayName("로그인 성공 통합 테스트")
    void loginSuccessIntegrationTest() {
        var loginDto = LoginDTO.builder()
                .email("user1@example.com")
                .password("password123!")
                .build();

        var userRespDTO = this.authService.login(loginDto);

        assertThat(userRespDTO).isNotNull();
        assertThat(userRespDTO.getUserEmail()).isEqualTo("user1@example.com");
    }

    @Test
    @DisplayName("로그인 실패 통합 테스트 - 잘못된 비밀번호")
    void loginFailureIntegrationTest_WrongPassword() {
        var loginDto = LoginDTO.builder()
                .email("user1@example.com")
                .password("wrongpassword")
                .build();

        var userRespDTO = this.authService.login(loginDto);

        assertThat(userRespDTO).isNull();
    }
}
