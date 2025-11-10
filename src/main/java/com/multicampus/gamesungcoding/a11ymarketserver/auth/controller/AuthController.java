package com.multicampus.gamesungcoding.a11ymarketserver.auth.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.auth.dto.LoginDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.auth.dto.UserRespDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.Users;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO dto, HttpSession session) {
        Users user = authService.login(dto);
        // 로그인 성공
        if (user != null) {
            session.setAttribute("loginUser", user);
            Map<String, Object> respBody = Map.of(
                    "msg", "로그인 성공",
                    "user", UserRespDTO.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .userEmail(user.getUserEmail())
                            .userNickname(user.getUserNickname())
                            .userRole(user.getUserRole())
                            .build()
            );
            return ResponseEntity.ok(respBody); // 200 ok와 JSON 반환
        } else {
            Map<String, String> errBody = Map.of(
                    "msg", "이메일 또는 비밀번호가 올바르지 않습니다."
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errBody);
        }
    }

    @PostMapping("/api/v1/auth/logout")
    @ResponseBody
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "로그아웃 성공";
    }


}
