package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.DefaultAddress;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DefaultAddressRepository extends JpaRepository<DefaultAddress, UUID> {
    // 기본 배송지 조회
    Optional<DefaultAddress> findByUserId(UUID userId);

    // 기본 배송지 삭제
    @Transactional
    void deleteByUserId(UUID userId);

    // 기본 배송지 유무 확인
    boolean existsByUserId(UUID userId);
}
