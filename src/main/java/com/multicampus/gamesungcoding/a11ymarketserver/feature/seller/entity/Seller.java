package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sellers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {

    @Id
    @UuidV7
    @Column(nullable = false, updatable = false, length = 16)
    private UUID sellerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @Column(nullable = false)
    private String sellerName;

    @Column(nullable = false)
    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SellerGrades sellerGrade;

    @Column(length = 1024)
    private String sellerIntro;

    @Column(nullable = false, name = "is_a11y_guarantee")
    private Boolean isA11yGuarantee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SellerSubmitStatus sellerSubmitStatus;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime submitDate;

    private LocalDateTime approvedDate;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private Seller(Users user,
                   String sellerName,
                   String businessNumber,
                   String sellerIntro) {

        this.user = user;
        this.sellerName = sellerName;
        this.businessNumber = businessNumber;
        this.sellerGrade = SellerGrades.NEWER;
        this.sellerIntro = sellerIntro;
        this.isA11yGuarantee = false;
        this.sellerSubmitStatus = SellerSubmitStatus.PENDING;
    }

    public void approve() {
        this.sellerSubmitStatus = SellerSubmitStatus.APPROVED;
        this.approvedDate = LocalDateTime.now();
        this.user.changeRole(UserRole.SELLER);
    }

    public void reject() {
        this.sellerSubmitStatus = SellerSubmitStatus.REJECTED;
        this.approvedDate = LocalDateTime.now();
    }

    public void updateAdminSellerInfo(String sellerName,
                                      String businessNumber,
                                      String sellerIntro,
                                      SellerGrades sellerGrade,
                                      Boolean a11yGuarantee) {

        if (sellerName != null) this.sellerName = sellerName;
        if (businessNumber != null) this.businessNumber = businessNumber;
        if (sellerIntro != null) this.sellerIntro = sellerIntro;
        if (sellerGrade != null) this.sellerGrade = sellerGrade;
        if (a11yGuarantee != null) this.isA11yGuarantee = a11yGuarantee;
    }
}

