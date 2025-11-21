package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.JwtResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.service.RefreshTokenService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.JoinRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest dto) {

        Users user = userRepository.findByUserEmail(dto.email())
                .orElseThrow(() -> new UserNotFoundException("이메일 또는 비밀번호가 올바르지 않습니다."));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        return LoginResponse.fromEntityAndTokens(
                user,
                jwtTokenProvider.createAccessToken(authentication),
                refreshTokenService.createRefreshToken(authentication)
        );
    }

    @Transactional
    public JwtResponse reissueToken(String refreshToken) {

        // get refresh token from DB and verifying validation
        var dbToken = refreshTokenService.verifyRefreshToken(refreshToken);
        var user = userRepository.findById(dbToken.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found for ID: " + dbToken.getUserId()));

        var userDetails = userDetailsService.loadUserByUsername(user.getUserEmail());

        var newAuthentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        String newAccessToken = jwtTokenProvider.createAccessToken(newAuthentication);
        return new JwtResponse(newAccessToken, refreshToken);
    }

    public UserResponse join(JoinRequest dto) {

        // 이메일 중복 체크
        if (userRepository.existsByUserEmail(dto.email())) {
            throw new DataDuplicatedException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPwd = passwordEncoder.encode(dto.password());

        Users user = Users.builder()
                .userEmail(dto.email())
                .userPass(encodedPwd)
                .userName(dto.username())
                .userNickname(dto.nickname())
                .userPhone(dto.phone())
                .userRole("USER")
                .build();
        return UserResponse.fromEntity(userRepository.save(user));
    }

    public void logout(String userEmail) {
        userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 사용자입니다."));
        refreshTokenService.deleteRefreshToken(userEmail);
    }
}
