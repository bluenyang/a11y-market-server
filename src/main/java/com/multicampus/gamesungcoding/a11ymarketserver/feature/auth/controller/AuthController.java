package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.JwtResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.RefreshRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.service.RefreshTokenService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.ErrorResponseDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.JoinRequestDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.JoinResponseDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.LoginDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/v1/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginDTO dto/*, HttpSession session*/) {
        // var user = authService.login(dto);
        // // 로그인 성공
        // if (user != null) {
        //     session.setAttribute("loginUser", user);
        //     return ResponseEntity.ok(user); // 200 ok와 JSON 반환
        // } else {
        //     var errBody = LoginErrResponse.builder()
        //             .message("이메일 또는 비밀번호가 올바르지 않습니다.")
        //             .build();
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errBody);
        // }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = refreshTokenService.createRefreshToken(authentication);

        return ResponseEntity.ok(
                JwtResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());
    }

    @PostMapping("/v1/auth/logout")
    @ResponseBody
    public ResponseEntity<String> logout(Authentication authentication) {
        String userEmail = authentication.getName();

        authService.logout(userEmail);
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/v1/auth/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        var resp = authService.reissueToken(refreshRequest.refreshToken());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/v1/auth/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinRequestDTO dto) {

        var savedUser = authService.join(dto);

        // 이메일 중복 시
        if (savedUser == null) {
            ErrorResponseDTO error = ErrorResponseDTO.builder()
                    .message("이미 존재하는 이메일입니다.")
                    .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        // 회원가입 성공 시
        JoinResponseDTO resp = JoinResponseDTO.builder()
                .msg("회원가입 성공")
                .user(savedUser)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


}

