package com.multicampus.gamesungcoding.a11ymarketserver.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRequest {
    @Size(min = 2, max = 30, message = "수령인 이름은 2~30자 여야 합니다.")
    private String userName;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 50)
    private String userEmail;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "휴대폰 번호는 010-0000-0000 형식이어야 합니다.")
    private String userPhone;

    @Size(min = 2, max = 20)
    private String userNickname;
}


