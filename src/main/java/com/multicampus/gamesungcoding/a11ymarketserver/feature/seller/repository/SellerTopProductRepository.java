package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.SellerTopProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SellerTopProductRepository extends JpaRepository<SellerTopProduct, UUID> {

    List<SellerTopProduct> findAllById_SellerIdAndSalesRankLessThanEqualOrderBySalesRankAsc(UUID id_sellerId, int salesRank);
}
