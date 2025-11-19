package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderRespDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrdersRepository ordersRepository;

    public List<AdminOrderRespDTO> getAllOrders() {
        return ordersRepository.findAll()
                .stream()
                .map(order -> new AdminOrderRespDTO(
                        order.getOrderId(),
                        order.getUserName(),
                        order.getUserEmail(),
                        order.getUserPhone(),
                        order.getReceiverName(),
                        order.getReceiverPhone(),
                        order.getReceiverZipcode(),
                        order.getReceiverAddr1(),
                        order.getReceiverAddr2(),
                        order.getOrderStatus(),
                        order.getTotalPrice(),
                        order.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
