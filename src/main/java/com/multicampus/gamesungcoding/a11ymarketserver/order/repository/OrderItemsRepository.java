package com.multicampus.gamesungcoding.a11ymarketserver.order.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.order.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemsRepository extends JpaRepository<OrderItems, UUID> {
}
