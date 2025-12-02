package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemsRepository extends JpaRepository<OrderItems, UUID> {
    // 특정 orderId 주문의 모든 상품 조회
    List<OrderItems> findAllByOrder_OrderId(UUID orderId);

    List<OrderItems> findAllByProduct_Seller_User_UserEmail_AndOrderItemStatus(
            String userEmail,
            OrderItemStatus status);

    boolean existsByOrder_OrderIdAndProduct_ProductIdIn(UUID orderId, List<UUID> productIds);

    List<OrderItems> findAllByProduct_Seller_User_UserEmail_AndOrderItemStatusIn(
            String userEmail,
            List<OrderItemStatus> statuses);

    List<OrderItems> findAllByProduct_ProductIdIn(List<UUID> list);
}
