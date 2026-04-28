package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.DailyRevenueDto
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerDashboardResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerOrderItemResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerTopProductResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerDashboardStats
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper.toSellerOrderItemResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper.toTopProductResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerDashboardRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerTopProductRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional(readOnly = true)
class SellerDashboardService(
    private val sellerRepository: SellerRepository,
    private val sellerDashboardRepository: SellerDashboardRepository,
    private val orderItemsRepository: OrderItemsRepository,
    private val sellerTopProductRepository: SellerTopProductRepository
) {

    @Transactional(readOnly = true)
    fun getDashboard(userEmail: String): SellerDashboardResponse {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("승인된 판매자만 대시보드를 조회할 수 있습니다.")
        }

        requireNotNull(seller.sellerId) {
            throw DataNotFoundException("조회된 객체의 Id는 null일 수 없습니다.")
        }

        val stats = sellerDashboardRepository.findBySellerId(seller.sellerId)

        return if (stats == null) {
            SellerDashboardResponse(
                seller.sellerId,
                seller.sellerName,
                seller.sellerIntro,
                null,
                null,
                null,
                null
            )
        } else {
            SellerDashboardResponse(
                seller.sellerId,
                seller.sellerName,
                seller.sellerIntro,
                stats.totalRevenue,
                stats.totalOrderCount,
                calculateRefundRate(stats),
                calculateConfirmationRate(stats)
            )
        }
    }

    fun getDailyRevenue(sellerEmail: String, year: Int, month: Int): List<DailyRevenueDto> {
        val result = orderItemsRepository.findDailyRevenue(
            getSellerIdByEmail(sellerEmail), year, month
        )

        return result.map { row ->
            val orderDate: LocalDateTime = (row[0] as? Timestamp)?.toLocalDateTime()
                ?: Timestamp((row[0] as Date).time).toLocalDateTime()

            val dailyRevenue = row[1] as BigDecimal
            DailyRevenueDto(orderDate, dailyRevenue)
        }
    }

    fun getTopProducts(sellerEmail: String, limit: Int): List<SellerTopProductResponse> {
        return sellerTopProductRepository
            .findAllByIdSellerIdAndSalesRankLessThanEqualOrderBySalesRankAsc(
                getSellerIdByEmail(sellerEmail),
                limit
            ).map { it.toTopProductResponse() }
    }

    private fun calculateRefundRate(stats: SellerDashboardStats): BigDecimal {
        if (stats.totalOrderCount == 0L) {
            return BigDecimal.ZERO
        }
        return BigDecimal.valueOf(stats.refundedCount)
            .divide(
                BigDecimal.valueOf(stats.totalOrderCount),
                4,
                RoundingMode.HALF_UP
            ) * BigDecimal.valueOf(100)
    }

    private fun calculateConfirmationRate(stats: SellerDashboardStats): BigDecimal {
        if (stats.totalOrderCount == 0L) {
            return BigDecimal.ZERO
        }
        return BigDecimal.valueOf(stats.confirmedCount)
            .divide(
                BigDecimal.valueOf(stats.totalOrderCount),
                4,
                RoundingMode.HALF_UP
            ) * BigDecimal.valueOf(100)
    }

    fun getRecentOrders(sellerEmail: String, page: Int, size: Int): List<SellerOrderItemResponse> {
        val sellerId = getSellerIdByEmail(sellerEmail)
        val pageable = PageRequest.of(page, size)

        return orderItemsRepository
            .findBySellerIdWithDetails(sellerId, pageable)
            .content
            .map { it.toSellerOrderItemResponse() }
    }

    private fun getSellerIdByEmail(userEmail: String): UUID =
        sellerRepository.findByUserUserEmail(userEmail)?.sellerId
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")
}
