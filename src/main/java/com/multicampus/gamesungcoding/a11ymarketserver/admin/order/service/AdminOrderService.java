package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model.AdminOrderResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminOrderService {

    private final OrdersRepository ordersRepository;

    public List<AdminOrderResponse> getAllOrders() {
        log.debug("AdminOrderService - getAllOrders: Fetching all orders from the repository");
        return ordersRepository.findAll()
                .stream()
                .map(AdminOrderResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
