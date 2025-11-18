package com.multicampus.gamesungcoding.a11ymarketserver.auth.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.JoinRequestDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final UUID mockUserId = UUID.randomUUID();
    private final String mockEmail = "user1@example.com";
    private Users mockUser;

    @BeforeEach
    void setUp() {
        // 추가적인 설정이 필요한 경우 여기에 작성
        this.mockUser = Users.builder()
                .userId(this.mockUserId)
                .userEmail(this.mockEmail)
                .userPass("encodedPassword")
                .userName("User One")
                .userNickname("user-one")
                .userPhone("010-1234-5678")
                .userRole("USER")
                .build();
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() {
        var loginDto = LoginDTO.builder()
                .email(this.mockEmail)
                .password("password123!")
                .build();

        given(this.userRepository.findByUserEmail(this.mockEmail))
                .willReturn(Optional.of(this.mockUser));
        given(this.passwordEncoder.matches(loginDto.getPassword(), this.mockUser.getUserPass()))
                .willReturn(true);

        var userRespDTO = this.authService.login(loginDto);

        assertThat(userRespDTO).isNotNull();
        assertThat(userRespDTO.getUserEmail()).isEqualTo(this.mockEmail);
        assertThat(userRespDTO.getUserId()).isEqualTo(this.mockUserId);
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
    void loginFailWrongPasswordTest() {
        var loginDto = LoginDTO.builder()
                .email(this.mockEmail)
                .password("WrongPassword!")
                .build();

        given(this.userRepository.findByUserEmail(this.mockEmail))
                .willReturn(Optional.of(this.mockUser));

        assertThat(this.authService.login(loginDto))
                .isNull();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void joinSuccessTest() {
        var joinDto = JoinRequestDTO.builder()
                .email(this.mockEmail)
                .password("password123!")
                .username("User One")
                .nickname("user-one")
                .phone("010-1234-5678")
                .build();

        given(this.userRepository.existsByUserEmail(this.mockEmail))
                .willReturn(false);
        given(this.passwordEncoder.encode(joinDto.getPassword()))
                .willReturn("encodedPassword");
        given(this.userRepository.save(any(Users.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        var userRespDTO = this.authService.join(joinDto);
        assertThat(userRespDTO).isNotNull();
        assertThat(userRespDTO.getUserEmail()).isEqualTo(this.mockEmail);
    }
}
