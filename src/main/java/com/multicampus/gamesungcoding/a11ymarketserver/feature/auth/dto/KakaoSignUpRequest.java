package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record KakaoSignUpRequest(
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "형식에 맞는 이메일 주소여야 합니다.")
        String userEmail,

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        String userName,

        String userNickname,

        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^[0-9]{10,15}$", message = "전화번호는 숫자만 포함해야 합니다.")
        @Size(min = 10, max = 15, message = "전화번호 형식이 잘못되었습니다.")
        String userPhone) {
}
