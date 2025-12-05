package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Getter
@Entity
@Immutable
@Table(name = "view_category_recommendations")
public class CategoryRecommendations {
    @Id
    @Column(columnDefinition = "RAW(16)")
    private UUID productId;

    @Column(name = "root_id", columnDefinition = "RAW(16)")
    private UUID rootCategoryId;

    @Column(name = "root_name")
    private String rootCategoryName;

    @Column
    private String productName;

    @Column
    private Integer productPrice;

    @Column
    private String productImageUrl;

    @Column
    private Long monthlySalesVolume;
}
