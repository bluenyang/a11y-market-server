package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model;

import com.multicampus.gamesungcoding.a11ymarketserver.common.converter.TrimmedStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressInfo {
    @Column(length = 100, nullable = false)
    @Size(max = 100, message = "주소명은 100자 이하여야 합니다.")
    private String addressName;

    @Column(length = 30, nullable = false)
    @Size(min = 2, max = 30, message = "수령인 이름은 2~30자여야 합니다.")
    private String receiverName;

    @Column(length = 13, nullable = false)
    @Pattern(regexp = "^\\d+$", message = "휴대폰 번호는 숫자여야 합니다.")
    private String receiverPhone;

    @Column(length = 5, nullable = false, columnDefinition = "CHAR(5)")
    @Convert(converter = TrimmedStringConverter.class)
    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
    private String receiverZipcode;

    @Column(length = 100, nullable = false)
    @Size(max = 100, message = "주소는 100자 이하여야 합니다.")
    private String receiverAddr1;

    @Column(length = 200)
    @Size(max = 200, message = "상세 주소는 200자 이하여야 합니다.")
    private String receiverAddr2;
}
