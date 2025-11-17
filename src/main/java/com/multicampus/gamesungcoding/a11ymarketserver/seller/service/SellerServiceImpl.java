package com.multicampus.gamesungcoding.a11ymarketserver.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.model.SellerApplyResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * [SellerServiceImpl]
 * - 판매자 비즈니스 로직 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    @Override
    public SellerApplyResponse applySeller(UUID userId, SellerApplyRequest request) {

        // 이미 판매자 신청 또는 등록 이력이 있는지 체크
        sellerRepository.findByUserId(userId).ifPresent(existing -> {
            // TODO: 공통 예외 타입으로 변경 예정 (예: CustomBusinessException 등)
            throw new IllegalStateException("이미 판매자이거나 판매자 신청 이력이 존재합니다.");
        });

        // 신규 Seller 엔티티 생성
        Seller seller = Seller.builder()
                .sellerId(UUID.randomUUID())              // PK (RAW(16) 매핑)
                .userId(userId)
                .sellerName(request.getSellerName())
                .businessNumber(request.getBusinessNumber())
                .sellerGrade("NEW")                       // 기본 등급
                .sellerIntro(request.getSellerIntro())
                .a11yGuarantee(request.getA11yGuarantee())
                .sellerSubmitStatus("pending")                // 기본 상태
                .build();

        Seller saved = sellerRepository.save(seller);

        // Entity → DTO 변환 (DTO는 Entity 타입을 모르게 유지)
        return SellerApplyResponse.builder()
                .sellerId(saved.getSellerId())
                .sellerName(saved.getSellerName())
                .businessNumber(saved.getBusinessNumber())
                .sellerGrade(saved.getSellerGrade())
                .sellerIntro(saved.getSellerIntro())
                .a11yGuarantee(saved.getA11yGuarantee())
                .sellerSubmitStatus(saved.getSellerSubmitStatus())
                .submitDate(saved.getSubmitDate())
                .approvedDate(saved.getApprovedDate())
                .build();
    }
}
