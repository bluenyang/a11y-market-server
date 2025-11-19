package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model;

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

/**
 * [Seller 엔티티]
 * - sellers 테이블과 매핑
 * - 판매자 기본 정보 및 승인 상태 관리
 * <p>
 * DB 명세서 매핑:
 * - seller id (PK): RAW(16)
 * - user id (FK): RAW(16) NOT NULL
 * - 상호명: VARCHAR(255) NOT NULL
 * - 사업자번호: VARCHAR(255) NOT NULL
 * - 판매자 등급: VARCHAR(30) NOT NULL
 * - 소개문구: VARCHAR(1024)
 * - 판매자 접근성 인증 여부: NUMBER(1) NOT NULL  → is_a11y_guarantee
 * - 승인 상태 ENUM(’pending’, ‘approved’, ‘rejected’): VARCHAR(20) NOT NULL
 * - 신청일: TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 * - 승인일: TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 * - updated at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 */
@Entity
@Table(name = "sellers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller {

    /**
     * 판매자 ID (PK, RAW(16))
     */
    @Id
    @UuidV7
    @Column(nullable = false, updatable = false, length = 16)
    private UUID sellerId;

    /**
     * 사용자 ID (FK → users.user_id, RAW(16))
     */
    @Column(nullable = false, length = 16)
    private UUID userId;

    /**
     * 상호명
     */
    @Column(nullable = false, length = 255)
    private String sellerName;

    /**
     * 사업자등록번호
     */
    @Column(nullable = false, length = 255)
    private String businessNumber;

    /**
     * 판매자 등급 (예: NEW, NORMAL, VIP 등)
     */
    @Column(nullable = false, length = 30)
    private String sellerGrade;

    /**
     * 판매자 소개 문구
     */
    @Column(length = 1024)
    private String sellerIntro;

    /**
     * 접근성 인증 여부
     * - DB 컬럼명: is_a11y_guarantee (NUMBER(1))
     */
    @Column(name = "is_A11y_Guarantee", nullable = false)
    private Boolean a11yGuarantee;

    /**
     * 승인 상태 (pending / approved / rejected)
     */
    @Column(nullable = false, length = 20)
    private String sellerSubmitStatus;

    /**
     * 신청일 (생성일시)
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime submitDate;

    /**
     * 승인일
     */
    private LocalDateTime approvedDate;

    /**
     * 최신 수정일
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    //============= 도메인 메서드 =============//

    /**
     * 판매자 정보 승인
     */
    public void approve() {
        this.sellerSubmitStatus = SellerSubmitStatus.APPROVED.getStatus();
        this.approvedDate = LocalDateTime.now();
    }

    /**
     * 판매자 정보 거절
     */
    public void reject() {
        this.sellerSubmitStatus = SellerSubmitStatus.REJECTED.getStatus();
        this.approvedDate = LocalDateTime.now();
    }
}

