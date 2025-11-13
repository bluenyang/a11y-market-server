package com.multicampus.gamesungcoding.a11ymarketserver.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.UserUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/v1/users/me")
    public ResponseEntity<UserResponse> getUserInfo(
            HttpSession session
            // @RequestParam String uuid
    ) {
        log.info("UserController getUserInfo");
        String uuid = (String) session.getAttribute("userId");
        log.debug("Session userId: {}", uuid);
        UUID userId = uuid != null ? UUID.fromString(uuid) : null;
        log.debug("Session userId: {}", userId);

        if (userId == null) {
            log.info("Session userId is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse response = userService.getUserInfo(userId);

        if (response == null) {
            log.info("User not found for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("User found: {}", response.getUserEmail());
        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정
    @PatchMapping("/v1/users/me")
    public ResponseEntity<UserResponse> updateUserInfo(
            HttpSession session,
            //@RequestParam String uuid,
            @Valid @RequestBody UserUpdateRequest request) {

        String uuid = (String) session.getAttribute("userId");
        UUID userId = uuid != null ? UUID.fromString(uuid) : null;

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse response = userService.updateUserInfo(userId, request);
        return ResponseEntity.ok(response);
    }
}
