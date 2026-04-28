package com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.service;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.dashboard.dto.AdminDashboardStats;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    public AdminDashboardStats getAdminDashboardStats() {
        int sellerPendingCount = this.sellerRepository.countBySellerSubmitStatus(SellerSubmitStatus.PENDING);
        int productPendingCount = this.productRepository.countByProductStatus(ProductStatus.PENDING);

        log.debug("Fetched Admin Dashboard Stats - Pending Sellers: {}, Pending Products: {}",
                sellerPendingCount, productPendingCount);

        return new AdminDashboardStats(sellerPendingCount, productPendingCount);
    }
}
