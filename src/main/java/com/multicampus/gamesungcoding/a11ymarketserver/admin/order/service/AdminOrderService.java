package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderStatus;
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

    public List<AdminOrderResponse> getAllOrders() {
        log.debug("AdminOrderService - getAllOrders: Fetching all orders from the repository");
        return ordersRepository.findAll()
                .stream()
                .map(AdminOrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public ResponseEntity<OrderDetailResponse> getOrderDetails(UUID orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        List<OrderItems> items = orderItemsRepository.findAllByOrderId(orderId);

        return ResponseEntity.ok(OrderDetailResponse.fromEntity(order, items));
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, String status) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        OrderStatus newStatus;

        try {
            newStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid order status: " + status);
        }

        order.updateOrderStatus(newStatus);
    }

}
