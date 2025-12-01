package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Categories, UUID> {
}
