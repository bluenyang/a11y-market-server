package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.JwtResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.RefreshRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.JoinRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    @PostMapping("/v1/auth/logout")
    @ResponseBody
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
    public ResponseEntity<UserResponse> join(
            @RequestBody @Valid JoinRequest dto) {

        return ResponseEntity
                .created(URI.create("/api/v1/users/me"))
                .body(authService.join(dto));
    }
}