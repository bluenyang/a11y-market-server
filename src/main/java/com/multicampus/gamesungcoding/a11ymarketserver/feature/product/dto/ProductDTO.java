package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import lombok.*;

import java.util.UUID;

/**
 * 상품 목록/검색 응답 DTO.
 * - 엔티티 직접 참조/의존 없음
 * - 현재 스코프에 필요한 최소 필드만 노출
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private UUID productId;
    private String productName;
    private Integer productPrice;
    private ProductStatus productStatus;

    public static ProductDTO fromEntity(Product entity) {
        return ProductDTO.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .productPrice(entity.getProductPrice())
                .productStatus(entity.getProductStatus())
                .build();
    }
}
