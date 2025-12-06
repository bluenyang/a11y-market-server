package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductAiSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductAiSummaryRepository extends JpaRepository<ProductAiSummary, UUID> {
    ProductAiSummary findAllByProduct(Product product);
}
