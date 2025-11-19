package com.multicampus.gamesungcoding.a11ymarketserver.admin.order.model;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AdminOrderRespDTO {
    private UUID orderId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String receiverName;
    private String receiverPhone;
    private String receiverZipcode;
    private String receiverAddr1;
    private String receiverAddr2;
    private OrderStatus orderStatus;
    private Integer totalPrice;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
}
