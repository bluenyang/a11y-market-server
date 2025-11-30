package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SellerSalesRepository extends JpaRepository<SellerSales, UUID> {

    Optional<SellerSales> findBySellerId(UUID sellerId);
}
