package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Categories, UUID> {

    @Query("SELECT c FROM Categories c ORDER BY c.parentCategory.categoryId ASC NULLS FIRST, c.categoryName ASC")
    List<Categories> findAllOrderByParentCategoryIdAscNullsFirst();

    List<Categories> findAllByParentCategoryIsNull();
}
