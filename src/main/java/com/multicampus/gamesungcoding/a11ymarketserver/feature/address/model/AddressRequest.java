package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddressRequest {
    @Size(max = 100)
    private String addressName;

    @Size(min = 2, max = 30, message = "수령인 이름은 2~30자여야 합니다.")
    private String receiverName;

    @Pattern(regexp = "^\\d+$", message = "휴대폰 번호는 숫자여야 합니다.")
    private String receiverPhone;

    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
    private String receiverZipcode;

    @Size(max = 100)
    private String receiverAddr1;

    @Size(max = 200)
    private String receiverAddr2;
}
