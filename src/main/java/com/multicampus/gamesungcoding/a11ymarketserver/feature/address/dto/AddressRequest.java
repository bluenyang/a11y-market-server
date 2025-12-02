package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @Size(max = 100, message = "주소명은 100자 이하여야 합니다.")
        String addressName,

        @Size(min = 2, max = 30, message = "수령인 이름은 2~30자여야 합니다.")
        String receiverName,

        @Pattern(regexp = "^\\d+$", message = "휴대폰 번호는 숫자여야 합니다.")
        String receiverPhone,

        @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
        String receiverZipcode,

        @Size(max = 100, message = "주소는 100자 이하여야 합니다.")
        String receiverAddr1,

        @Size(max = 200, message = "상세 주소는 200자 이하여야 합니다.")
        String receiverAddr2,
        
        Boolean isDefault) {
}
