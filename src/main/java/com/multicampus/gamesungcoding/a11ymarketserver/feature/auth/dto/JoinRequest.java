package com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JoinRequest(
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "형식에 맞는 이메일 주소여야 합니다.")
        String userEmail,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        // Complex passwords can be difficult to remember for users with cognitive constraints.
        //    @Pattern(
        //            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        //            message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다."
        //    )
        String userPass,

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        String userName,

        String userNickname,

        @Size(min = 10, max = 15, message = "전화번호 형식이 잘못되었습니다.")
        String userPhone) {

}
