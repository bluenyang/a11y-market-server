package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserA11yProfileReq
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserA11yProfileResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.A11yProfileInfo
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserA11yProfile
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.mapper.toA11yProfileResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserA11yProfileRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class UserA11yProfileService(
    private val profileRepository: UserA11yProfileRepository,
    private val userRepository: UserRepository

) {
    // 프로필 목록 조회
    fun getMyProfiles(userEmail: String): List<UserA11yProfileResponse> {
        val user = getUserByEmail(userEmail)
        val list: List<UserA11yProfile> = profileRepository.findAllByUserOrderByCreatedAtAsc(user)
        return list.map { it.toA11yProfileResponse() }.toList()
    }


    // 프로필 생성
    @Transactional
    fun createProfile(userEmail: String, dto: UserA11yProfileReq): UserA11yProfileResponse {
        val user = getUserByEmail(userEmail)

        // 프로필 이름 중복 체크
        if (profileRepository.existsByUserAndProfileInfoProfileName(user, dto.profileName)) {
            throw InvalidRequestException("이미 존재하는 프로필 이름입니다.")
        }

        val profile = UserA11yProfile(
            user = user,
            profileInfo = A11yProfileInfo(
                profileName = dto.profileName,
                description = dto.description,
                contrastLevel = dto.contrastLevel,
                textSizeLevel = dto.textSizeLevel,
                textSpacingLevel = dto.textSpacingLevel,
                lineHeightLevel = dto.lineHeightLevel,
                textAlign = dto.textAlign,
                screenReader = dto.screenReader,
                smartContrast = dto.smartContrast,
                highlightLinks = dto.highlightLinks,
                cursorHighlight = dto.cursorHighlight
            )
        )

        return profileRepository.save(profile).toA11yProfileResponse()
    }

    // 프로필 수정
    @Transactional
    fun updateProfile(userEmail: String, profileId: UUID, dto: UserA11yProfileReq) {
        val user = getUserByEmail(userEmail)

        val profile: UserA11yProfile = profileRepository.findByProfileIdAndUser(profileId, user)
            ?: throw DataNotFoundException("해당 접근성 프로필을 찾을 수 없습니다.")

        // 프로필 이름 중복 체크
        if (profile.profileInfo.profileName != dto.profileName &&
            profileRepository.existsByUserAndProfileInfoProfileName(user, dto.profileName)
        ) {
            throw InvalidRequestException("이미 존재하는 프로필 이름입니다.")
        }

        val info = A11yProfileInfo(
            profileName = dto.profileName,
            description = dto.description,
            contrastLevel = dto.contrastLevel,
            textSizeLevel = dto.textSizeLevel,
            textSpacingLevel = dto.textSpacingLevel,
            lineHeightLevel = dto.lineHeightLevel,
            textAlign = dto.textAlign,
            screenReader = dto.screenReader,
            smartContrast = dto.smartContrast,
            highlightLinks = dto.highlightLinks,
            cursorHighlight = dto.cursorHighlight
        )

        profile.update(info)
    }

    // 프로필 삭제
    @Transactional
    fun deleteProfile(userEmail: String, profileId: UUID) {
        val deleted = profileRepository.deleteByProfileIdAndUser(
            profileId = profileId,
            user = getUserByEmail(userEmail)
        )

        if (deleted == 0L) {
            throw DataNotFoundException("삭제할 수 있는 프로필이 없습니다.")
        }
    }

    // UserEmail -> UserId 변환
    private fun getUserByEmail(email: String): Users =
        userRepository.findByUserEmail(email)
            ?: throw UserNotFoundException("해당 사용자를 찾을 수 없습니다")
}
