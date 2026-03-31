package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "carts")
class Cart(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    var user: Users
) {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    var cartId: UUID? = null
}
