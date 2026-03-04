package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class JoinRequest(
    @field:NotBlank(message = "이메일은 필수 입력 값입니다.")
    @field:Email(message = "형식에 맞는 이메일 주소여야 합니다.")
    val userEmail: String,

    @field:NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    val userPass: String,

    @field:NotBlank(message = "이름은 필수 입력 값입니다.")
    val userName: String,

    val userNickname: String,

    @field:Size(min = 10, max = 15, message = "전화번호 형식이 잘못되었습니다.")
    val userPhone: String
)
