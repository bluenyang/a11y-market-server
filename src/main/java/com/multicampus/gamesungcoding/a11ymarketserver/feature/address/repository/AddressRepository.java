package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Addresses, UUID> {
    // 전체 배송지 조회 (최신순)
    List<Addresses> findByUser_UserEmailOrderByCreatedAtDesc(String userEmail);

    Optional<Addresses> findByUser_UserEmail(String userEmail);

    Optional<Addresses> findByUser_UserEmailAndIsDefaultTrue(String userUserEmail);

    // 사용자 특정 배송지 조회
    Optional<Addresses> findByAddressIdAndUser_UserEmail(UUID addressId, String userEmail);

    // 사용자 이메일로 배송지 조회
    List<Addresses> findAllByUser_UserEmail(String userEmail);
}
