package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service.AdminUserManageService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserAdminResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminUserManageController {
    private final AdminUserManageService userService;

    // 관리자 - 전체 사용자 조회
    @GetMapping("/v1/admin/users")
    public ResponseEntity<List<UserAdminResponse>> inquireUsers() {

        var userList = userService.listAll();
        return ResponseEntity.ok(userList);
    }

    // 관리자 - 사용자 권한 변경
    @PatchMapping("/v1/admin/users/{userId}/permission")
    public ResponseEntity<UserResponse> changeUserPermission(
            @PathVariable UUID userId,
            @RequestParam UserRole role) {

        return ResponseEntity.ok(userService.changePermission(userId, role));
    }
}
