package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.dto.JwtResponse
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.service.RefreshTokenService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.status.CheckExistsStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserOauthLinksRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenService: RefreshTokenService,
    private val userDetailsService: UserDetailsService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
    private val userOauthLinksRepository: UserOauthLinksRepository,
) {
    fun login(dto: LoginRequest): LoginResponse {
        val user = userRepository.findByUserEmail(dto.email)
            .orElseThrow { UserNotFoundException("이메일 또는 비밀번호가 올바르지 않습니다.") }

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(dto.email, dto.password)
        )

        return LoginResponse.fromEntityAndTokens(
            user,
            jwtTokenProvider.createAccessToken(authentication),
            refreshTokenService.createRefreshToken(authentication)
        )
    }

    fun loginRefresh(refreshToken: String): LoginResponse {
        // get refresh token from DB and verifying validation
        val user = getUserByRefreshToken(refreshToken)
        val userDetails = userDetailsService.loadUserByUsername(user.userEmail)

        val newAuthentication = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        )

        val newAccessToken = jwtTokenProvider.createAccessToken(newAuthentication)

        return LoginResponse.fromEntityAndTokens(
            user,
            newAccessToken,
            refreshToken
        )
    }

    fun getUserInfo(userId: UUID): LoginResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("유효하지 않은 사용자입니다.") }

        val authorities = listOf(
            SimpleGrantedAuthority(user.userRole.name)
        )

        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            user.userEmail,
            null,
            authorities
        )

        return LoginResponse.fromEntityAndTokens(
            user,
            jwtTokenProvider.createAccessToken(authentication),
            refreshTokenService.createRefreshToken(authentication)
        )
    }

    @Transactional
    fun reissueToken(refreshToken: String): JwtResponse {
        val user = getUserByRefreshToken(refreshToken)
        val userDetails = userDetailsService.loadUserByUsername(user.userEmail)

        val newAuthentication = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        )

        val newAccessToken = jwtTokenProvider.createAccessToken(newAuthentication)
        return JwtResponse(newAccessToken, refreshToken)
    }

    @Transactional
    fun join(dto: JoinRequest): UserResponse {
        // 이메일 중복 체크
        if (userRepository.existsByUserEmail(dto.userEmail)) {
            throw DataDuplicatedException("이미 존재하는 이메일입니다.")
        }

        // 비밀번호 암호화
        val encodedPwd = passwordEncoder.encode(dto.userPass)

        val user = Users.builder()
            .userEmail(dto.userEmail)
            .userPass(encodedPwd)
            .userName(dto.userName)
            .userNickname(dto.userNickname)
            .userPhone(dto.userPhone)
            .userRole(UserRole.USER)
            .build()
        return UserResponse.fromEntity(userRepository.save(user))
    }

    @Transactional
    fun kakaoJoin(userOauthLinkId: UUID, dto: KakaoSignUpRequest): UserResponse {
        if (userRepository.existsByUserEmail(dto.userEmail)) {
            throw DataDuplicatedException("이미 존재하는 이메일입니다.")
        }

        var oauthLink = userOauthLinksRepository.findById(userOauthLinkId)
            .orElseThrow { DataNotFoundException("OAuth link not found for ID: $userOauthLinkId") }
        if (oauthLink.user != null) {
            throw InvalidRequestException("이미 가입된 OAuth 링크입니다.")
        }

        val user = userRepository.save(
            Users.builder()
                .userEmail(dto.userEmail)
                .userName(dto.userName)
                .userNickname(dto.userNickname)
                .userRole(UserRole.USER)
                .build()
        )
        oauthLink.updateUser(user)
        oauthLink = userOauthLinksRepository.save(oauthLink)

        return UserResponse.fromEntity(oauthLink.user)
    }

    // 이메일 중복 체크 API용
    fun isEmailDuplicate(email: String): CheckExistsResponse {
        val status =
            if (userRepository.existsByUserEmail(email)) CheckExistsStatus.UNAVAILABLE
            else CheckExistsStatus.AVAILABLE
        return CheckExistsResponse(status)
    }

    fun isPhoneDuplicate(phone: String): CheckExistsResponse {
        val status =
            if (userRepository.existsByUserPhone(phone)) CheckExistsStatus.UNAVAILABLE
            else CheckExistsStatus.AVAILABLE
        return CheckExistsResponse(status)
    }

    fun isNicknameDuplicate(nickname: String): CheckExistsResponse {
        val status =
            if (userRepository.existsByUserNickname(nickname)) CheckExistsStatus.UNAVAILABLE
            else CheckExistsStatus.AVAILABLE
        return CheckExistsResponse(status)
    }

    fun logout(userEmail: String) {
        userRepository.findByUserEmail(userEmail)
            .orElseThrow { InvalidRequestException("유효하지 않은 사용자입니다.") }
        refreshTokenService.deleteRefreshToken(userEmail)
    }

    private fun getUserByRefreshToken(refreshToken: String): Users {
        val dbToken = refreshTokenService.verifyRefreshToken(refreshToken)
        return userRepository.findById(dbToken.user.userId)
            .orElseThrow {
                DataNotFoundException("User not found for: " + dbToken.user.userEmail)
            }
    }
}