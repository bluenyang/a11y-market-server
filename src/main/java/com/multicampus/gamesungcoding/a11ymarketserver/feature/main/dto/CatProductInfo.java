package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto;

import java.util.UUID;

public record CatProductInfo(UUID productId,
                             String productName,
                             Integer productPrice,
                             String productImageUrl) {
}
