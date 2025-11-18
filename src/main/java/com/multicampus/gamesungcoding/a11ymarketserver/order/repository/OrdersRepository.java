package com.multicampus.gamesungcoding.a11ymarketserver.order.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrdersRepository extends JpaRepository<Orders, UUID> {
}
