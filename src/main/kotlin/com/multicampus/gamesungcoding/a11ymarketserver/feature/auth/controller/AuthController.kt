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
    fun login(@RequestBody dto: LoginRequest): ResponseEntity<LoginResponse> =
        ResponseEntity.ok(authService.login(dto))


    @PostMapping("/v1/auth/login-refresh")
    fun loginRefresh(@Valid @RequestBody dto: RefreshRequest): ResponseEntity<LoginResponse> =
        ResponseEntity.ok(authService.loginRefresh(dto.refreshToken))


    @PostMapping("/v1/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Void> {
        authService.logout(userDetails.username)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/v1/auth/refresh")
    fun refreshToken(@Valid @RequestBody refreshRequest: RefreshRequest): ResponseEntity<JwtResponse> =
        ResponseEntity.ok(authService.reissueToken(refreshRequest.refreshToken))


    @PostMapping("/v1/auth/join")
    @ResponseStatus(HttpStatus.CREATED)
    fun join(@RequestBody @Valid dto: JoinRequest): ResponseEntity<UserResponse> =
        ResponseEntity
            .created(URI.create("/api/v1/users/me"))
            .body(authService.join(dto))

    @PostMapping("/v1/auth/kakao-join")
    @ResponseStatus(HttpStatus.CREATED)
    fun kakaoJoin(
        @AuthenticationPrincipal principal: UserDetails,
        @RequestBody dto: KakaoSignUpRequest
    ): ResponseEntity<UserResponse> =
        ResponseEntity
            .created(URI.create("/api/v1/users/me"))
            .body(authService.kakaoJoin(UUID.fromString(principal.username), dto))

    @GetMapping("/v1/auth/me/info")
    fun getLoginUserInfo(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<LoginResponse> =
        ResponseEntity.ok(authService.getUserInfo(UUID.fromString(userDetails.username)))


    @GetMapping("/v1/auth/check/email")
    fun checkEmail(
        @RequestParam email: String
    ): ResponseEntity<CheckExistsResponse> =
        ResponseEntity.ok(authService.isEmailDuplicate(email))

    @GetMapping("/v1/auth/check/phone")
    fun checkPhone(@RequestParam phone: String): ResponseEntity<CheckExistsResponse> =
        ResponseEntity.ok(authService.isPhoneDuplicate(phone))

    @GetMapping("/v1/auth/check/nickname")
    fun checkNickname(@RequestParam nickname: String): ResponseEntity<CheckExistsResponse> =
        ResponseEntity.ok(authService.isNicknameDuplicate(nickname))
}