package com.multicampus.gamesungcoding.a11ymarketserver.user.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddressResponse {

    private UUID addressId;
    private UUID userId;
    private String receiverName;
    private String receiverPhone;
    private Integer receiverZipcode;
    private String receiverAddr1;
    private String receiverAddr2;
    private LocalDateTime createdAt;

    public static AddressResponse from(Address address) {
        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .userId(address.getUserId())
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .receiverZipcode(address.getReceiverZipcode())
                .receiverAddr1(address.getReceiverAddr1())
                .receiverAddr2(address.getReceiverAddr2())
                .createdAt(address.getCreatedAt())
                .build();
    }

}
