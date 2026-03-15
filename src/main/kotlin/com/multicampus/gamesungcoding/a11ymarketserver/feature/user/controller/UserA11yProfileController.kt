package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.controller

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserA11yProfileReq
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserA11yProfileResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserA11yProfileService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users/me/a11y")
class UserA11yProfileController(
    private val profileService: UserA11yProfileService
) {

    // 접근성 프로필 목록 조회
    @GetMapping("/profiles")
    fun getMyProfiles(
        @AuthenticationPrincipal userDetails: UserDetails
    ): MutableList<UserA11yProfileResponse> =
        profileService.getMyProfiles(userDetails.username)


    // 접근성 프로필 생성
    @PostMapping("/profiles")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody req: UserA11yProfileReq,
        response: HttpServletResponse
    ): UserA11yProfileResponse {
        val saved = profileService.createProfile(userDetails.username, req)
        response.setHeader("Location", "/api/v1/users/me/a11y/profiles/${saved.profileId}")
        return saved
    }


    // 접근성 프로필 전체 수정
    @PutMapping("/profiles/{profileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable profileId: String,
        @Valid @RequestBody req: @Valid UserA11yProfileReq
    ) {
        val id: UUID? = try {
            UUID.fromString(profileId)
        } catch (_: IllegalArgumentException) {
            throw InvalidRequestException("잘못된 UUID 형식입니다.")
        }
        profileService.updateProfile(userDetails.username, id, req)
    }

    // 접근성 프로필 삭제
    @DeleteMapping("/profiles/{profileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable profileId: String
    ) {
        val id: UUID = try {
            UUID.fromString(profileId)
        } catch (e: IllegalArgumentException) {
            throw InvalidRequestException("잘못된 UUID 형식입니다.")
        }
        profileService.deleteProfile(userDetails.username, id)
    }
}