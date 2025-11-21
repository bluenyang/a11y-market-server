package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public record AddressResponse(UUID addressId, UUID userId, String addressName,
                              String receiverName, String receiverPhone,
                              String receiverZipcode, String receiverAddr1,
                              String receiverAddr2, LocalDateTime createdAt) {

    public static AddressResponse fromEntity(Addresses address) {
        return new AddressResponse(
                address.getAddressId(),
                address.getUserId(),
                address.getAddress().getAddressName(),
                address.getAddress().getReceiverName(),
                address.getAddress().getReceiverPhone(),
                address.getAddress().getReceiverZipcode(),
                address.getAddress().getReceiverAddr1(),
                address.getAddress().getReceiverAddr2(),
                address.getCreatedAt());
    }

}
