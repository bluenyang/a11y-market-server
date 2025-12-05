package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CategoryResponse {
    private UUID categoryId;
    private String categoryName;

    @Builder.Default
    private List<CategoryResponse> subCategories = new ArrayList<>();

    public static CategoryResponse fromEntity(Categories category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }

    public void addSubCategory(CategoryResponse subCategory) {
        this.subCategories.add(subCategory);
    }
}
