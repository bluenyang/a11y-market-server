package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.JwtResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.RefreshRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/v1/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest dto) {

        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/v1/auth/login-refresh")
    public ResponseEntity<LoginResponse> loginRefresh(@RequestBody RefreshRequest dto) {
        return ResponseEntity.ok(authService.loginRefresh(dto.refreshToken()));
    }

    @PostMapping("/v1/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> logout(
            @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        if (userEmail == null) {
            return ResponseEntity.noContent().build();
        }

        authService.logout(userEmail);
        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("/v1/auth/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @RequestBody RefreshRequest refreshRequest) {

        return ResponseEntity.ok(
                authService.reissueToken(refreshRequest.refreshToken())
        );
    }

    @PostMapping("/v1/auth/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> join(
            @RequestBody @Valid JoinRequest dto) {

        return ResponseEntity
                .created(URI.create("/api/v1/users/me"))
                .body(authService.join(dto));
    }

    @PostMapping("/v1/auth/kakao-join")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> kakaoJoin(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody KakaoSignUpRequest dto) {

        var userOauthLinkId = UUID.fromString(principal.getUsername());

        return ResponseEntity
                .created(URI.create("/api/v1/users/me"))
                .body(authService.kakaoJoin(userOauthLinkId, dto));
    }

    @GetMapping("/v1/auth/me/info")
    public ResponseEntity<LoginResponse> getLoginUserInfo(
            @AuthenticationPrincipal UserDetails userDetails) {

        var response = authService.getUserInfo(
                UUID.fromString(userDetails.getUsername()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/auth/check/email")
    public ResponseEntity<CheckExistsResponse> checkEmail(@RequestParam String email) {
        var exists = authService.isEmailDuplicate(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/v1/auth/check/phone")
    public ResponseEntity<CheckExistsResponse> checkPhone(@RequestParam String phone) {
        var exists = authService.isPhoneDuplicate(phone);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/v1/auth/check/nickname")
    public ResponseEntity<CheckExistsResponse> checkNickname(@RequestParam String nickname) {
        var exists = authService.isNicknameDuplicate(nickname);
        return ResponseEntity.ok(exists);
    }
}