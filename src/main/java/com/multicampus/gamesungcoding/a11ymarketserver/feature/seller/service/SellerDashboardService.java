package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.DailyRevenueDto;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerDashboardResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerOrderItemResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerTopProductResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerDashboardStats;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerDashboardRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerTopProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerDashboardService {
    private final SellerRepository sellerRepository;
    private final SellerDashboardRepository sellerDashboardRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final SellerTopProductRepository sellerTopProductRepository;

    @Transactional(readOnly = true)
    public SellerDashboardResponse getDashboard(String userEmail) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("승인된 판매자만 대시보드를 조회할 수 있습니다.");
        }

        var stats = sellerDashboardRepository.findBySellerId(seller.getSellerId())
                .orElseThrow(() -> new DataNotFoundException("판매자 대시보드 통계 정보를 찾을 수 없습니다."));

        return new SellerDashboardResponse(
                seller.getSellerId(),
                seller.getSellerName(),
                stats.getTotalRevenue(),
                stats.getTotalOrderCount(),
                calculateRefundRate(stats),
                calculateConfirmationRate(stats)
        );
    }

    public List<DailyRevenueDto> getDailyRevenue(String sellerEmail, int year, int month) {
        var result = orderItemsRepository.findDailyRevenue(
                getSellerIdByEmail(sellerEmail), year, month);

        return result.stream()
                .map(row -> {
                    LocalDateTime orderDate;
                    if (row[0] instanceof Timestamp) {
                        orderDate = ((Timestamp) row[0]).toLocalDateTime();
                    } else {
                        orderDate = new Timestamp(((Date) row[0]).getTime()).toLocalDateTime();
                    }

                    BigDecimal dailyRevenue = (BigDecimal) row[1];

                    return new DailyRevenueDto(orderDate, dailyRevenue);
                })
                .toList();
    }

    public List<SellerTopProductResponse> getTopProducts(String sellerEmail, int limit) {
        return sellerTopProductRepository
                .findAllById_SellerIdAndSalesRankLessThanEqualOrderBySalesRankAsc(
                        getSellerIdByEmail(sellerEmail),
                        limit
                )
                .stream()
                .map(SellerTopProductResponse::fromEntity)
                .toList();
    }

    private BigDecimal calculateRefundRate(SellerDashboardStats stats) {
        if (stats.getTotalOrderCount() == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(stats.getRefundedCount())
                .divide(BigDecimal.valueOf(stats.getTotalOrderCount()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private BigDecimal calculateConfirmationRate(SellerDashboardStats stats) {
        if (stats.getTotalOrderCount() == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(stats.getConfirmedCount())
                .divide(BigDecimal.valueOf(stats.getTotalOrderCount()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public List<SellerOrderItemResponse> getRecentOrders(String sellerEmail, int page, int size) {
        var uuid = getSellerIdByEmail(sellerEmail);

        Pageable pageable = PageRequest.of(page, size);
        var result = orderItemsRepository.findBySellerIdWithDetails(uuid, pageable);
        var items = result.getContent();
        return items.stream()
                .map(SellerOrderItemResponse::fromEntity)
                .toList();

    }

    private UUID getSellerIdByEmail(String userEmail) {
        return sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."))
                .getSellerId();
    }
}
