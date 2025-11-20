package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 판매자 비즈니스 로직 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SellerService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public SellerApplyResponse applySeller(String userEmail, SellerApplyRequest request) {
        Users user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        // 이미 판매자 신청 또는 등록 이력이 있는지 체크
        sellerRepository.findByUserId(user.getUserId()).ifPresent(existing -> {
            throw new DataDuplicatedException("이미 판매자이거나 판매자 신청 이력이 존재합니다.");
        });

        // 신규 Seller 엔티티 생성
        Seller seller = Seller.builder()
                .userId(user.getUserId())
                .sellerName(request.sellerName())
                .businessNumber(request.businessNumber())
                .sellerGrade(SellerGrades.NEWER.getGrade())
                .sellerIntro(request.sellerIntro())
                .a11yGuarantee(false)
                .sellerSubmitStatus(SellerSubmitStatus.PENDING.name())
                .build();

        Seller saved = sellerRepository.save(seller);

        return new SellerApplyResponse(
                saved.getSellerId(),
                saved.getSellerName(),
                saved.getBusinessNumber(),
                saved.getSellerGrade(),
                saved.getSellerIntro(),
                saved.getA11yGuarantee(),
                saved.getSellerSubmitStatus(),
                saved.getSubmitDate(),
                saved.getApprovedDate());
    }

    public ProductDTO registerProduct(String userEmail, SellerProductRegisterRequest request) {

        // userId 로 판매자 조회
        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보가 존재하지 않습니다. 먼저 판매자 가입 신청을 완료하세요."));

        // 판매자 승인 여부 확인
        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품 등록이 가능합니다.");
        }

        // Product 엔티티 생성
        UUID sellerId = seller.getSellerId();
        UUID categoryId = UUID.fromString(request.categoryId());

        Product product = Product.builder()
                .sellerId(sellerId)
                .categoryId(categoryId)
                .productName(request.productName())
                .productDescription(request.productDescription())
                .productPrice(request.productPrice())
                .productStock(request.productStock())
                // 관리자 승인 대기 상태
                .productStatus(ProductStatus.PENDING)
                .build();

        // 저장 및 DTO 변환 후 반환
        return ProductDTO.fromEntity(productRepository.save(product));
    }

    // 내 상품 목록 조회
    @Transactional(readOnly = true)
    public List<ProductDTO> getMyProducts(String userEmail) {

        // 이메일로 판매자 찾기
        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        UUID sellerId = seller.getSellerId();

        // 판매자의 상품 목록 조회
        List<Product> products = productRepository.findBySellerId(sellerId);

        // DTO 변환 후 반환
        return products.stream()
                .map(ProductDTO::fromEntity)
                .toList();
    }

    // 상품 수정 요청
    @Transactional
    public ProductDTO updateProduct(
            String userEmail,
            UUID productId,
            SellerProductUpdateRequest request
    ) {

        // 판매자 조회 (없으면 404)
        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        // 승인된 판매자인지 확인 (미승인 → 400)
        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품을 수정할 수 있습니다.");
        }

        // 상품 조회 (없으면 404)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        // 본인 상품인지 확인 (아니면 400)
        if (!product.getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품만 수정할 수 있습니다.");
        }

        // 실제 수정 적용
        product.updateBySeller(
                UUID.fromString(request.categoryId()),
                request.productName(),
                request.productDescription(),
                request.productPrice(),
                request.productStock()
        );

        // 저장 후 DTO 반환
        return ProductDTO.fromEntity(productRepository.save(product));
    }
}