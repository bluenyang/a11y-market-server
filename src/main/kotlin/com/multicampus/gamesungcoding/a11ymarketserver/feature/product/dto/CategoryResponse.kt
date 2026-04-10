package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories
import java.util.*

class CategoryResponse(
    val categoryId: UUID,
    val categoryName: String
) {
    val subCategories = arrayListOf<CategoryResponse>()

    fun addSubCategory(subCategory: CategoryResponse) {
        this.subCategories.add(subCategory)
    }


    companion object {
        fun fromEntity(category: Categories): CategoryResponse? {
            return CategoryResponse(category.categoryId, category.categoryName)
        }
    }
}
