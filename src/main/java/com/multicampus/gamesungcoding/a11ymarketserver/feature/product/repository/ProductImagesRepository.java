package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductImagesRepository extends JpaRepository<ProductImages, UUID> {
    Optional<ProductImages> findByProductId(UUID productId);
}
