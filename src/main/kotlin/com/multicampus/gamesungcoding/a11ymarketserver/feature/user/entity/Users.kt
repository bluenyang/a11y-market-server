package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserUpdateRequest
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class Users(
    @Column(length = 30)
    var userName: String,

    @Column(length = 50)
    var userEmail: String,

    @Column(length = 20)
    var userNickname: String,

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    var userRole: UserRole,

    @Column(length = 100)
    var userPass: String? = null,

    @Column(length = 15)
    var userPhone: String? = null,
) {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    val userId: UUID? = null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null
        private set

    @OneToOne(mappedBy = "user")
    var seller: Seller? = null
        private set

    // 회원 정보 수정
    fun updateUserInfo(request: UserUpdateRequest) {
        request.userName?.let { this.userName = it }
        request.userEmail?.let { this.userEmail = it }
        request.userPhone?.let { this.userPhone = it }
        request.userNickname?.let { this.userNickname = it }
    }

    // 비밀번호 변경 메소드
    fun updatePassword(encodedPassword: String) {
        this.userPass = encodedPassword
    }

    // 사용자 권한 변경 메소드
    fun changeRole(role: UserRole) {
        this.userRole = role
    }
}
