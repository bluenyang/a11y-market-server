package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.SellerOrderItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderItemsRepository extends JpaRepository<OrderItems, UUID> {
    // 특정 orderId 주문의 모든 상품 조회
    List<OrderItems> findByOrderId(UUID orderId);

    @Query("""
            SELECT new com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.SellerOrderItemResponse(o, oi)
              FROM OrderItems oi, Orders o, Product p, Seller s, Users u
             WHERE oi.orderId = o.orderId
               AND oi.productId = p.productId
               AND p.sellerId = s.sellerId
               AND s.userId = u.userId
               AND u.userEmail = :userEmail
               AND (:status IS NULL OR oi.orderItemStatus = :status)
            """)
    List<SellerOrderItemResponse> findSellerReceivedOrders(
            @Param("userEmail") String userEmail,
            @Param("status") OrderItemStatus status);

    boolean existsByOrderIdAndProductIdIn(UUID orderId, List<UUID> productIds);

    @Query("""
            SELECT new com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.SellerOrderItemResponse(o, oi)
              FROM OrderItems oi, Orders o, Product p, Seller s, Users u
             WHERE oi.orderId = o.orderId
               AND oi.productId = p.productId
               AND p.sellerId = s.sellerId
               AND s.userId = u.userId
               AND u.userEmail = :userEmail
               AND oi.orderItemStatus IN :statuses
            """)
    List<SellerOrderItemResponse> findSellerClaims(
            @Param("userEmail") String userEmail,
            @Param("statuses") List<OrderItemStatus> statuses);
}
