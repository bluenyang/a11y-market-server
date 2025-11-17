package com.multicampus.gamesungcoding.a11ymarketserver.seller.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * [SellerRepository]
 * - Seller 엔티티 기본 CRUD 및 사용자 기준 조회
 */
public interface SellerRepository extends JpaRepository<Seller, UUID> {

    /**
     * userId 기준으로 판매자 정보 조회
     * - 한 사용자당 하나의 Seller만 가진다고 가정
     */
    Optional<Seller> findByUserId(UUID userId);
}
