package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sellers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller {

    @Id
    @UuidV7
    @Column(nullable = false, updatable = false, length = 16)
    private UUID sellerId;

    @Column(nullable = false, length = 16)
    private UUID userId;

    @Column(nullable = false, length = 255)
    private String sellerName;

    @Column(nullable = false, length = 255)
    private String businessNumber;

    @Column(nullable = false, length = 30)
    private String sellerGrade;

    @Column(length = 1024)
    private String sellerIntro;

    @Column(name = "is_A11y_Guarantee", nullable = false)
    private Boolean a11yGuarantee;

    @Column(nullable = false, length = 20)
    private String sellerSubmitStatus;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime submitDate;

    private LocalDateTime approvedDate;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void approve() {
        this.sellerSubmitStatus = SellerSubmitStatus.APPROVED.getStatus();
        this.approvedDate = LocalDateTime.now();
    }

    public void reject() {
        this.sellerSubmitStatus = SellerSubmitStatus.REJECTED.getStatus();
        this.approvedDate = LocalDateTime.now();
    }

    public void updateAdminSellerInfo(String sellerName,
                                      String businessNumber,
                                      String sellerIntro,
                                      String sellerGrade,
                                      Boolean a11yGuarantee) {
        if (sellerName != null) this.sellerName = sellerName;
        if (businessNumber != null) this.businessNumber = businessNumber;
        if (sellerIntro != null) this.sellerIntro = sellerIntro;
        if (sellerGrade != null) this.sellerGrade = sellerGrade;
        if (a11yGuarantee != null) this.a11yGuarantee = a11yGuarantee;
    }
}

