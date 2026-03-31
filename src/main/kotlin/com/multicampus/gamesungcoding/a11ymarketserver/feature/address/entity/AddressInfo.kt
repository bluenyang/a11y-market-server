package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.converter.TrimmedStringConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Embeddable
class AddressInfo(
    @Column(length = 100, nullable = false)
    @field:Size(max = 100, message = "주소명은 100자 이하여야 합니다.")
    var addressName: String,

    @Column(length = 30, nullable = false)
    @field:Size(min = 2, max = 30, message = "수령인 이름은 2~30자여야 합니다.")
    var receiverName: String,

    @Column(length = 13, nullable = false)
    @field:Pattern(regexp = "^\\d+$", message = "휴대폰 번호는 숫자여야 합니다.")
    var receiverPhone: String,

    @Column(length = 5, nullable = false, columnDefinition = "CHAR(5)")
    @Convert(converter = TrimmedStringConverter::class)
    @field:Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
    var receiverZipcode: String,

    @Column(length = 100, nullable = false)
    @field:Size(max = 100, message = "주소는 100자 이하여야 합니다.")
    var receiverAddr1: String,

    @Column(length = 200)
    @field:Size(max = 200, message = "상세 주소는 200자 이하여야 합니다.")
    var receiverAddr2: String? = null
)
