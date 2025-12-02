package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSubmitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {

    Optional<Seller> findByUser_UserId(UUID userId);

    Optional<Seller> findByUser_UserEmail(String userEmail);

    // sellerSubmitStatus가 pending인 만매자 조회
    List<Seller> findAllBySellerSubmitStatus(SellerSubmitStatus status);
}
