package com.multicampus.gamesungcoding.a11ymarketserver.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private UUID addressId;
    private String receiverName;
    private String receiverPhone;
    private String receiverZipcode;
    private String receiverAddr1;
    private String receiverAddr2;
}
