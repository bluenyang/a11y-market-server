package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderItemsRepository extends JpaRepository<OrderItems, UUID> {
    // 특정 orderId 주문의 모든 상품 조회
    List<OrderItems> findAllByOrder_OrderId(UUID orderId);

    List<OrderItems> findAllByProduct_Seller_User_UserEmail_AndOrderItemStatus(
            String userEmail,
            OrderItemStatus status);

    boolean existsByOrderItemIdAndProduct_ProductIdIn(UUID orderItemId, List<UUID> productIds);

    List<OrderItems> findAllByProduct_Seller_User_UserEmail_AndOrderItemStatusIn(
            String userEmail,
            List<OrderItemStatus> statuses);

    List<OrderItems> findAllByProduct_Seller_User_UserEmail(String userEmail);

    Boolean existsByProduct_Seller_User_UserEmail_AndOrderItemStatusIn(String userEmail, List<OrderItemStatus> statuses);

    @Query(value = """
            SELECT
                TRUNC(o.CREATED_AT) as orderDate,
                SUM(oi.PRODUCT_PRICE * oi.PRODUCT_QUANTITY) as dailyRevenue
            FROM ORDER_ITEMS oi
            JOIN ORDERS o ON oi.ORDER_ID = o.ORDER_ID
            JOIN PRODUCTS p ON oi.PRODUCT_ID = p.PRODUCT_ID
            WHERE p.SELLER_ID = :sellerId
              AND oi.ORDER_ITEM_STATUS = 'CONFIRMED'
              AND EXTRACT(YEAR FROM o.CREATED_AT) = :year
              AND EXTRACT(MONTH FROM o.CREATED_AT) = :month
            GROUP BY TRUNC(o.CREATED_AT)
            ORDER BY TRUNC(o.CREATED_AT)
            """, nativeQuery = true)
    List<Object[]> findDailyRevenue(
            @Param("sellerId") UUID sellerId,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query(value = """
             SELECT oi
             FROM OrderItems oi
             JOIN FETCH oi.product p
             JOIN FETCH oi.order o
             WHERE p.seller.sellerId = :sellerId
             ORDER BY o.createdAt DESC
            """,
            countQuery = """
                     SELECT count(oi) FROM OrderItems oi
                     JOIN oi.product p
                     WHERE p.seller.sellerId = :sellerId
                    """)
    Page<OrderItems> findBySellerIdWithDetails(UUID sellerId, Pageable pageable);
}
