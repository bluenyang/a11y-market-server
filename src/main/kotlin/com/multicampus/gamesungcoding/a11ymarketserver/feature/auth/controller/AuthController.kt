package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.controller

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.JwtResponse
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.RefreshRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/api")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/v1/auth/login")
    fun login(@RequestBody dto: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(authService.login(dto))
    }

    @PostMapping("/v1/auth/login-refresh")
    fun loginRefresh(@RequestBody dto: RefreshRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(authService.loginRefresh(dto.refreshToken))
    }

    @PostMapping("/v1/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<String> {
        val userEmail = userDetails.username

        authService.logout(userEmail)
        return ResponseEntity.ok("SUCCESS")
    }

    @PostMapping("/v1/auth/refresh")
    fun refreshToken(
        @RequestBody refreshRequest: RefreshRequest
    ): ResponseEntity<JwtResponse> {
        return ResponseEntity.ok(
            authService.reissueToken(refreshRequest.refreshToken)
        )
    }

    @PostMapping("/v1/auth/join")
    @ResponseStatus(HttpStatus.CREATED)
    fun join(
        @RequestBody @Valid dto: JoinRequest
    ): ResponseEntity<UserResponse> {
        return ResponseEntity
            .created(URI.create("/api/v1/users/me"))
            .body(authService.join(dto))
    }

    @PostMapping("/v1/auth/kakao-join")
    @ResponseStatus(HttpStatus.CREATED)
    fun kakaoJoin(
        @AuthenticationPrincipal principal: UserDetails,
        @RequestBody dto: KakaoSignUpRequest
    ): ResponseEntity<UserResponse> {
        val userOauthLinkId = UUID.fromString(principal.username)

        return ResponseEntity
            .created(URI.create("/api/v1/users/me"))
            .body(authService.kakaoJoin(userOauthLinkId, dto))
    }

    @GetMapping("/v1/auth/me/info")
    fun getLoginUserInfo(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<LoginResponse> {
        val response = authService.getUserInfo(
            UUID.fromString(userDetails.username)
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/v1/auth/check/email")
    fun checkEmail(@RequestParam email: String): ResponseEntity<CheckExistsResponse> {
        val exists = authService.isEmailDuplicate(email)
        return ResponseEntity.ok(exists)
    }

    @GetMapping("/v1/auth/check/phone")
    fun checkPhone(@RequestParam phone: String): ResponseEntity<CheckExistsResponse> {
        val exists = authService.isPhoneDuplicate(phone)
        return ResponseEntity.ok(exists)
    }

    @GetMapping("/v1/auth/check/nickname")
    fun checkNickname(@RequestParam nickname: String): ResponseEntity<CheckExistsResponse> {
        val exists = authService.isNicknameDuplicate(nickname)
        return ResponseEntity.ok(exists)
    }
}