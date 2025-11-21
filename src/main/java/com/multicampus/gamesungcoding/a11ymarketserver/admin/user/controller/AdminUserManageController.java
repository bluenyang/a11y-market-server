package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service.AdminUserManageService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserAdminDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
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
    public ResponseEntity<List<UserAdminDTO>> inquireUsers() {

        var userList = userService.listAll();
        log.info("AdminUserManageController - inquireUsers: Retrieved {} users", userList.size());
        return ResponseEntity.ok(userList);
    }

    // 관리자 - 사용자 권한 변경
    @PatchMapping("/v1/admin/users/{userId}/permission")
    public ResponseEntity<UserResponse> changeUserPermission(
            @PathVariable UUID userId,
            @RequestParam String role) {

        return ResponseEntity.ok(userService.changePermission(userId, role));
    }
}
