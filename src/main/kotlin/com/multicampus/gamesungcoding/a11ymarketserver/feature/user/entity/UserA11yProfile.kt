package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "USER_A11Y_PROFILES")
@EntityListeners(AuditingEntityListener::class)
class UserA11yProfile(
    @Embedded
    var profileInfo: A11yProfileInfo,

    @OnDelete(action = OnDeleteAction.CASCADE) @JoinColumn(
        name = "user_id",
        nullable = false,
        updatable = false
    )
    @ManyToOne(fetch = FetchType.LAZY)
    val user: Users? = null,
) {
    @Id
    @UuidV7
    @Column(nullable = false, updatable = false, length = 16)
    val profileId: UUID? = null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null

    fun update(profileInfo: A11yProfileInfo) {
        this.profileInfo = profileInfo
    }
}
