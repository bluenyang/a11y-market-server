package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class KakaoSignUpRequest(
    @field:NotBlank(message = "이메일은 필수 입력 값입니다.")
    @field:Email(message = "형식에 맞는 이메일 주소여야 합니다.")
    val userEmail: String,

    @field:NotBlank(message = "이름은 필수 입력 값입니다.")
    val userName: String,

    val userNickname: String? = null,

    @field:NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @field:Pattern(regexp = "^[0-9]{10,15}$", message = "전화번호는 숫자만 포함해야 합니다.")
    val userPhone: String
)
