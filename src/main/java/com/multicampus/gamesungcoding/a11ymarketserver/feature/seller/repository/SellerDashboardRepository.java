package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerDashboardStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SellerDashboardRepository extends JpaRepository<SellerDashboardStats, UUID> {
    Optional<SellerDashboardStats> findBySellerId(UUID sellerId);
}
