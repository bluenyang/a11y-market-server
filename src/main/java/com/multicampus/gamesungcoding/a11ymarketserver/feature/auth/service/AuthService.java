package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.JwtResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.service.RefreshTokenService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.status.CheckExistsStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserOauthLinksRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    private final UserOauthLinksRepository userOauthLinksRepository;

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

    public LoginResponse loginRefresh(String refreshToken) {

        // get refresh token from DB and verifying validation
        var user = getUserByRefreshToken(refreshToken);
        var userDetails = userDetailsService.loadUserByUsername(user.getUserEmail());

        var newAuthentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        String newAccessToken = jwtTokenProvider.createAccessToken(newAuthentication);

        return LoginResponse.fromEntityAndTokens(
                user,
                newAccessToken,
                refreshToken
        );
    }

    public LoginResponse getUserInfo(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유효하지 않은 사용자입니다."));

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getUserRole().name())
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUserEmail(),
                null,
                authorities
        );

        return LoginResponse.fromEntityAndTokens(
                user,
                jwtTokenProvider.createAccessToken(authentication),
                refreshTokenService.createRefreshToken(authentication)
        );
    }

    @Transactional
    public JwtResponse reissueToken(String refreshToken) {

        var user = getUserByRefreshToken(refreshToken);
        var userDetails = userDetailsService.loadUserByUsername(user.getUserEmail());

        var newAuthentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        String newAccessToken = jwtTokenProvider.createAccessToken(newAuthentication);
        return new JwtResponse(newAccessToken, refreshToken);
    }

    @Transactional
    public UserResponse join(JoinRequest dto) {

        // 이메일 중복 체크
        if (userRepository.existsByUserEmail(dto.userEmail())) {
            throw new DataDuplicatedException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPwd = passwordEncoder.encode(dto.userPass());

        Users user = Users.builder()
                .userEmail(dto.userEmail())
                .userPass(encodedPwd)
                .userName(dto.userName())
                .userNickname(dto.userNickname())
                .userPhone(dto.userPhone())
                .userRole(UserRole.USER)
                .build();
        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserResponse kakaoJoin(UUID userOauthLinkId, KakaoSignUpRequest dto) {
        if (userRepository.existsByUserEmail(dto.userEmail())) {
            throw new DataDuplicatedException("이미 존재하는 이메일입니다.");
        }

        var oauthLink = userOauthLinksRepository.findById(userOauthLinkId)
                .orElseThrow(() -> new DataNotFoundException("OAuth link not found for ID: " + userOauthLinkId));
        if (oauthLink.getUser() != null) {
            throw new InvalidRequestException("이미 가입된 OAuth 링크입니다.");
        }

        Users user = userRepository.save(
                Users.builder()
                        .userEmail(dto.userEmail())
                        .userName(dto.userName())
                        .userNickname(dto.userNickname())
                        .userRole(UserRole.USER)
                        .build());
        oauthLink.updateUser(user);
        oauthLink = userOauthLinksRepository.save(oauthLink);

        return UserResponse.fromEntity(oauthLink.getUser());
    }

    // 이메일 중복 체크 API용
    public CheckExistsResponse isEmailDuplicate(String email) {
        if (userRepository.existsByUserEmail(email)) {
            return new CheckExistsResponse(CheckExistsStatus.UNAVAILABLE);
        } else {
            return new CheckExistsResponse(CheckExistsStatus.AVAILABLE);
        }
    }

    public CheckExistsResponse isPhoneDuplicate(String phone) {
        if (userRepository.existsByUserPhone(phone)) {
            return new CheckExistsResponse(CheckExistsStatus.UNAVAILABLE);
        } else {
            return new CheckExistsResponse(CheckExistsStatus.AVAILABLE);
        }
    }

    public CheckExistsResponse isNicknameDuplicate(String nickname) {
        if (userRepository.existsByUserNickname(nickname)) {
            return new CheckExistsResponse(CheckExistsStatus.UNAVAILABLE);
        } else {
            return new CheckExistsResponse(CheckExistsStatus.AVAILABLE);
        }
    }

    public void logout(String userEmail) {
        userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 사용자입니다."));
        refreshTokenService.deleteRefreshToken(userEmail);
    }

    private Users getUserByRefreshToken(String refreshToken) {
        var dbToken = refreshTokenService.verifyRefreshToken(refreshToken);
        return userRepository.findById(dbToken.getUser().getUserId())
                .orElseThrow(() ->
                        new DataNotFoundException("User not found for: " + dbToken.getUser().getUserEmail()));
    }
}
