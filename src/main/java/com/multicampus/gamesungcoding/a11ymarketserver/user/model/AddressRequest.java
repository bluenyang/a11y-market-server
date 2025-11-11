package com.multicampus.gamesungcoding.a11ymarketserver.user.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddressRequest {
    @Size(min = 2, max = 30, message = "수령인 이름은 2~30자여야 합니다.")
    private String receiverName;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "휴대폰 번호는 010-0000-0000 형식이어야 합니다.")
    private String receiverPhone;

    private Integer receiverZipcode;

    @Size(max = 100)
    private String receiverAddr1;

    @Size(max = 200)
    private String receiverAddr2;
}
