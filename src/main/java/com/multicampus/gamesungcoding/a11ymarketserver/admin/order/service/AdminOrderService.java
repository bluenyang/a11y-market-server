package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderSearchRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminOrderService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;

    public List<AdminOrderResponse> getOrders(AdminOrderSearchRequest request) {
        List<Orders> results = ordersRepository.searchOrders(
                request.searchType(),
                request.keyword(),
                request.status(),
                request.startDate(),
                request.endDate()
        );

        return results.stream()
                .map(AdminOrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public ResponseEntity<OrderDetailResponse> getOrderDetails(UUID orderItemId) {
        var item = orderItemsRepository.findById(orderItemId)
                .orElseThrow(() -> new DataNotFoundException("Order item not found"));

        return ResponseEntity.ok(OrderDetailResponse.fromEntity(item));
    }

    @Transactional
    public void updateOrderItemStatus(UUID orderItemId, OrderItemStatus status) {
        var orderItem = orderItemsRepository.findById(orderItemId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        orderItem.updateOrderItemStatus(status);
    }

}
