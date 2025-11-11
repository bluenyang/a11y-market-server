package com.multicampus.gamesungcoding.a11ymarketserver.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.user.model.UserRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/v1/users/me")
    public ResponseEntity<UserResponse> getUserInfo(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정
    @PatchMapping("/v1/users/me")
    public ResponseEntity<UserResponse> updateUserInfo(
            HttpSession session,
            @Valid @RequestBody UserRequest request) {

        UUID userId = (UUID) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse response = userService.updateUserInfo(userId, request);
        return ResponseEntity.ok(response);
    }


}
