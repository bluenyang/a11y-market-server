package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.CategoryRecommendations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecommendationRepository extends JpaRepository<CategoryRecommendations, UUID> {
}
