package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class CartItems
    (
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "cart_id", updatable = false, nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var cart: Cart,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", updatable = false, nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var product: Product?,

    @Column(nullable = false)
    var quantity: Int
) {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    private var cartItemId: UUID? = null // 장바구니 아이템 PK

    fun changeQuantity(quantity: Int) {
        require(quantity >= 1) { "quantity must be >= 1" }
        this.quantity = quantity
    }
}