package com.multicampus.gamesungcoding.a11ymarketserver.admin.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.SellerApplyResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.SellerSubmitStatus;
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

    public List<SellerApplyResponse> inquirePendingSellers() {

        List<Seller> pendingList = sellerRepository.findBySellerSubmitStatus(SellerSubmitStatus.PENDING.getStatus());

        return pendingList.stream()
                .map(SellerApplyResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void updateSellerStatus(UUID sellerId, String status) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new DataNotFoundException("Seller not found"));

        SellerSubmitStatus newStatus;
        try {
            newStatus = SellerSubmitStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid seller status: " + status);
        }

        switch (newStatus) {
            case APPROVED -> seller.approve();
            case REJECTED -> seller.reject();
        }
    }

}
