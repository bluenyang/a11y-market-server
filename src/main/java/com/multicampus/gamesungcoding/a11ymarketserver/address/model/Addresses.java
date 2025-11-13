package com.multicampus.gamesungcoding.a11ymarketserver.address.model;

import com.multicampus.gamesungcoding.a11ymarketserver.common.converter.TrimmedStringConverter;
import com.multicampus.gamesungcoding.a11ymarketserver.config.id.UuidV7;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Addresses {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    private UUID addressId;

    @Column(length = 16, updatable = false, nullable = false)
    private UUID userId;

    @Column(length = 100)
    private String addressName;

    @Column(length = 30)
    private String receiverName;

    @Column(length = 13)
    private String receiverPhone;

    @Column(length = 5, columnDefinition = "CHAR(5)")
    @Convert(converter = TrimmedStringConverter.class)
    private String receiverZipcode;

    @Column(length = 100)
    private String receiverAddr1;

    @Column(length = 200)
    private String receiverAddr2;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 배송지 정보 수정
    public void updateAddrInfo(
            String addressName, String receiverName,
            String receiverPhone, String receiverZipcode,
            String receiverAddr1, String receiverAddr2
    ) {
        if (addressName != null) this.addressName = addressName;
        if (receiverName != null) this.receiverName = receiverName;
        if (receiverPhone != null) this.receiverPhone = receiverPhone;
        if (receiverZipcode != null) this.receiverZipcode = receiverZipcode;
        if (receiverAddr1 != null) this.receiverAddr1 = receiverAddr1;
        if (receiverAddr2 != null) this.receiverAddr2 = receiverAddr2;
    }
}
