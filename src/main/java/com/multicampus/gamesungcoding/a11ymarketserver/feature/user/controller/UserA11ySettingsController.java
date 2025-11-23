package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.controller;


import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11ySettings;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserA11yUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserA11ySettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserA11ySettingsController {

    private final UserA11ySettingsService userA11ySettingsService;

    //내 접근성 설정 조회
    @GetMapping("/v1/users/me/a11y")
    public ResponseEntity<UserA11ySettings> getMyA11ySettings(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(userA11ySettingsService.getMySettings(email));
    }

    // 내 접근성 설정 저장/수정
    @PutMapping("/v1/users/me/a11y")
    public ResponseEntity<Void> updateMyA11ySettings(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserA11yUpdateRequest request
    ) {
        String email = userDetails.getUsername();
        userA11ySettingsService.updateMySettings(email, request);
        return ResponseEntity.noContent().build();
    }


}
