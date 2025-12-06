package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerTopProductId implements Serializable {
    @Column
    private UUID sellerId;

    @Column
    UUID productId;
}
