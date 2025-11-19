package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT s FROM Seller s
                    WHERE s.userId = (
                        SELECT u.userId FROM Users u
                        WHERE u.userEmail = :email
                    )
            """)
    Optional<Seller> findByUserEmail(@Param("email") String userEmail);
}
