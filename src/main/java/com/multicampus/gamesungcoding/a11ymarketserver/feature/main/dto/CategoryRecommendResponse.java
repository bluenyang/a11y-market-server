package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CategoryRecommendResponse {
    private UUID categoryId;
    private String categoryName;

    @Builder.Default
    private List<CatProductInfo> products = new ArrayList<>();

    public static CategoryRecommendResponse fromEntity(Categories category) {
        return CategoryRecommendResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }

    public void addProduct(CatProductInfo product) {
        this.products.add(product);
    }
}
