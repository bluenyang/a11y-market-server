package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserUpdateRequest;
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
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Users {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    private UUID userId;

    @Column(length = 30)
    private String userName;

    @Column(length = 100)
    private String userPass;

    @Column(length = 50)
    private String userEmail;

    @Column(length = 15)
    private String userPhone;

    @Column(length = 20)
    private String userNickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private UserRole userRole;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user")
    private Seller seller;


    // 회원 정보 수정
    public void updateUserInfo(UserUpdateRequest request) {
        if (request.userName() != null) {
            this.userName = request.userName();
        }
        if (request.userEmail() != null) {
            this.userEmail = request.userEmail();
        }
        if (request.userPhone() != null) {
            this.userPhone = request.userPhone();
        }
        if (request.userNickname() != null) {
            this.userNickname = request.userNickname();
        }
    }

    // 비밀번호 변경 메소드
    public void updatePassword(String encodedPassword) {
        this.userPass = encodedPassword;
    }

    // 사용자 권한 변경 메소드
    public void changeRole(UserRole role) {
        this.userRole = role;
    }

}
