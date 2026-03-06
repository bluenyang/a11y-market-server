package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.controller

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.JwtResponse
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.RefreshRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody dto: LoginRequest): LoginResponse =
        authService.login(dto)


    @PostMapping("/login-refresh")
    fun loginRefresh(@Valid @RequestBody dto: RefreshRequest): LoginResponse =
        authService.loginRefresh(dto.refreshToken)


    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Void> {
        authService.logout(userDetails.username)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody refreshRequest: RefreshRequest): JwtResponse =
        authService.reissueToken(refreshRequest.refreshToken)


    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    fun join(
        @RequestBody @Valid dto: JoinRequest,
        response: HttpServletResponse
    ): UserResponse {
        response.setHeader("Location", "/api/me")
        return authService.join(dto)
    }

    @PostMapping("/kakao-join")
    @ResponseStatus(HttpStatus.CREATED)
    fun kakaoJoin(
        @AuthenticationPrincipal principal: UserDetails,
        @RequestBody dto: KakaoSignUpRequest,
        response: HttpServletResponse
    ): UserResponse {
        response.setHeader("Location", "/api/me")
        return authService.kakaoJoin(UUID.fromString(principal.username), dto)
    }

    @GetMapping("/me/info")
    fun getLoginUserInfo(
        @AuthenticationPrincipal userDetails: UserDetails
    ): LoginResponse =
        authService.getUserInfo(UUID.fromString(userDetails.username))


    @GetMapping("/check/email")
    fun checkEmail(
        @RequestParam email: String
    ): CheckExistsResponse =
        authService.isEmailDuplicate(email)

    @GetMapping("/check/phone")
    fun checkPhone(@RequestParam phone: String): CheckExistsResponse =
        authService.isPhoneDuplicate(phone)

    @GetMapping("/check/nickname")
    fun checkNickname(@RequestParam nickname: String): CheckExistsResponse =
        authService.isNicknameDuplicate(nickname)
}