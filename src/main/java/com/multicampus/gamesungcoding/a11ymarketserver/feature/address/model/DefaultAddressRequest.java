package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DefaultAddressRequest {
    @NotNull(message = "addressId는 필수입니다.")
    private UUID addressId;
}

