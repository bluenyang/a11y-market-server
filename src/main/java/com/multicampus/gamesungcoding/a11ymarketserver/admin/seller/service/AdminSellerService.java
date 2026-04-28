package com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.model.AdminSellerUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminSellerService {

    private final SellerRepository sellerRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;

    public List<SellerProfileResponse> getAllSellerProfile() {
        List<Seller> sellers = sellerRepository.findAll();
        return sellers.stream()
                .map(SellerProfileResponse::fromEntity)
                .toList();
    }

    public SellerDetailResponse getSellerProfile(UUID sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new DataNotFoundException("Seller not found"));

        var orderItems = orderItemsRepository.findAllByProduct_Seller(seller);

        return SellerDetailResponse.fromEntity(seller, orderItems);
    }

    public List<SellerApplyResponse> inquirePendingSellers() {

        List<Seller> pendingList = sellerRepository.findAllBySellerSubmitStatus(SellerSubmitStatus.PENDING);

        return pendingList.stream()
                .map(SellerApplyResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void updateSellerStatus(UUID sellerId, SellerSubmitStatus status) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new DataNotFoundException("Seller not found"));

        switch (status) {
            case APPROVED -> seller.approve();
            case REJECTED -> seller.reject();
        }
    }

    @Transactional
    public void updateSellerInfo(UUID sellerId, AdminSellerUpdateRequest request) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new DataNotFoundException("Seller not found"));

        seller.updateAdminSellerInfo(
                request.sellerName(),
                request.businessNumber(),
                request.sellerIntro(),
                request.sellerGrade() != null ? request.sellerGrade() : null,
                request.a11yGuarantee()
        );
    }


}
