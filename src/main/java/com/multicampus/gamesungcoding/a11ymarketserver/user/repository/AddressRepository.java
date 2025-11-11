package com.multicampus.gamesungcoding.a11ymarketserver.user.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.user.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    // 전체 배송지 조회 (최신순)
    List<Address> findByUserIdOrderByCreatedAtDesc(UUID userId);

    // 사용자 특정 배송지 조회
    Optional<Address> findByAddressIdAndUserId(UUID addressId, UUID userId);

}
