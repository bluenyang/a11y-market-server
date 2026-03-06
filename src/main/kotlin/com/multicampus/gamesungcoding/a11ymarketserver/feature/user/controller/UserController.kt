package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserDeleteRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserUpdateRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/api/v1/users/")
class UserController(
    private val userService: UserService
) {
    // 회원 정보 조회
    @GetMapping("/me")
    fun getUserInfo(@AuthenticationPrincipal userDetails: UserDetails): UserResponse =
        userService.getUserInfo(userDetails.username)


    // 회원 정보 수정
    @PatchMapping("/me")
    fun updateUserInfo(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: UserUpdateRequest
    ): UserResponse =
        userService.updateUserInfo(userDetails.username, request)


    @PostMapping("/me/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody req: UserDeleteRequest
    ) {
        userService.deleteUser(userDetails.username, req)
    }

}