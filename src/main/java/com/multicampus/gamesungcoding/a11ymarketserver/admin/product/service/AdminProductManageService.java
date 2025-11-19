package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminProductManageService {
    private final ProductRepository productRepository;

    public List<ProductDTO> inquirePendingProducts() {
        log.info("inquirePendingProducts - called");
        var list = this.productRepository.findAll(
                (root, query, criteriaBuilder) ->
                        // !! column 이름이 아니라 entity 필드 이름으로 작성히야 함 !!
                        criteriaBuilder.equal(root.get("productStatus"), "PENDING")

        );

        if (list.isEmpty()) {
            log.info("inquirePendingProducts - list is empty");
            return List.of();
        }

        log.info("found {} pending products", list.size());
        return list.stream()
                .map(ProductDTO::fromEntity)
                .toList();
    }

    // 관리자 - 상품 상태 변경 (미구현)
    @Transactional
    public void changeProductStatus(UUID productId, ProductStatus status) {
        log.info("changeProductStatus - called");

        var user = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        user.changeStatus(status);
        log.info("changeProductStatus - product status changed to {}", status);
    }
}
