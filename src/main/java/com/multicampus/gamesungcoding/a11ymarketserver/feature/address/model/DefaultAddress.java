package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "default_addresses")
public class DefaultAddress {
    @Id
    @Column(length = 16, nullable = false)
    private UUID userId;

    @Column(length = 16, nullable = false)
    private UUID addressId;

    public void changeDefaultAddress(UUID addressId) {
        this.addressId = addressId;
    }
}
