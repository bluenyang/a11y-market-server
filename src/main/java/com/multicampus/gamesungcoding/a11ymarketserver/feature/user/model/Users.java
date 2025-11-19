package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model;

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

    @Column(length = 30)
    private String userRole;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 회원 정보 수정
    public void updateUserInfo(UserUpdateRequest request) {
        if (request.getUserName() != null) {
            this.userName = request.getUserName();
        }
        if (request.getUserEmail() != null) {
            this.userEmail = request.getUserEmail();
        }
        if (request.getUserPhone() != null) {
            this.userPhone = request.getUserPhone();
        }
        if (request.getUserNickname() != null) {
            this.userNickname = request.getUserNickname();
        }
    }

    // 비밀번호 변경 메소드
    public void updatePassword(String encodedPassword) {
        this.userPass = encodedPassword;
    }

    // 사용자 권한 변경 메소드
    public void changeRole(String role) {
        this.userRole = role;
    }

}
