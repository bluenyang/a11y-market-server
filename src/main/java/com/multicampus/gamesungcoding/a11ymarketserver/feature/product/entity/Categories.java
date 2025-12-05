package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Categories {
    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    private UUID categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_cat_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Categories parentCategory;

    @Column(length = 200, nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @Builder
    private Categories(Categories parentCategory,
                       String categoryName) {
        
        this.parentCategory = parentCategory;
        this.categoryName = categoryName;
    }
}
