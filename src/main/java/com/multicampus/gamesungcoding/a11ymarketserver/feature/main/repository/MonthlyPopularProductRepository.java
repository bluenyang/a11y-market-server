package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MonthlyPopularProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MonthlyPopularProductRepository extends JpaRepository<MonthlyPopularProduct, UUID> {
    List<MonthlyPopularProduct> findTop10ByOrderByRankingAsc();
}
