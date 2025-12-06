package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DailyRevenueDto(LocalDateTime orderDate,
                              BigDecimal dailyRevenue) {
}
