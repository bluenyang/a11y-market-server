package com.multicampus.gamesungcoding.a11ymarketserver.auth.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.service.RefreshTokenService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.JoinRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginRequest;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;

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
        var loginDto = new LoginRequest(
                this.mockEmail,
                "password123!");

        given(this.userRepository.findByUserEmail(this.mockEmail))
                .willReturn(Optional.of(this.mockUser));

        UserDetails mockUserDetails =
                User.withUsername(this.mockEmail)
                        .password("Password123!")
                        .roles("USER")
                        .build();
        Authentication mockAuth =
                new UsernamePasswordAuthenticationToken(
                        "user1@exampmle.com",
                        "Password123!",
                        mockUserDetails.getAuthorities()
                );

        given(this.authenticationManager.authenticate(any()))
                .willReturn(mockAuth);
        given(this.jwtTokenProvider.createAccessToken(any()))
                .willReturn("mockAccessToken");
        given(this.refreshTokenService.createRefreshToken(any()))
                .willReturn("mockRefreshToken");

        var userRespDTO = this.authService.login(loginDto);

        assertThat(userRespDTO).isNotNull();
        assertThat(userRespDTO.userEmail()).isEqualTo(this.mockEmail);
        assertThat(userRespDTO.userRole()).isEqualTo("USER");
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
    void loginFailWrongPasswordTest() {
        var loginDto = new LoginRequest(
                this.mockEmail,
                "WrongPassword!");

        given(this.userRepository.findByUserEmail(this.mockEmail))
                .willReturn(Optional.of(this.mockUser));
        given(this.authenticationManager.authenticate(any()))
                .willThrow(BadCredentialsException.class);

        assertThatThrownBy(() ->
                this.authService.login(loginDto)
        ).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void joinSuccessTest() {
        var joinDto = new JoinRequest(
                this.mockEmail,
                "password123!",
                "User One",
                "user-one",
                "010-1234-5678");

        given(this.userRepository.existsByUserEmail(this.mockEmail))
                .willReturn(false);
        given(this.passwordEncoder.encode(joinDto.password()))
                .willReturn("encodedPassword");
        given(this.userRepository.save(any(Users.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        var userRespDTO = this.authService.join(joinDto);
        assertThat(userRespDTO).isNotNull();
        assertThat(userRespDTO.getUserEmail()).isEqualTo(this.mockEmail);
    }
}
