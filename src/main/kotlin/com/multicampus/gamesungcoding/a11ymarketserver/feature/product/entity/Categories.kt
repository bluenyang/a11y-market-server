package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*

@Entity
@Table(name = "categories")
@EntityListeners(AuditingEntityListener::class)
class Categories(
    @Column(length = 200, nullable = false)
    val categoryName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_cat_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val parentCategory: Categories? = null
) {
    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    var categoryId: UUID? = null
        private set

    @OneToMany(mappedBy = "category")
    var products: MutableList<Product> = mutableListOf()
        private set
}
