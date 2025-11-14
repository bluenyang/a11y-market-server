package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductManageServiceIntTest {
    @Autowired
    private AdminProductManageService service;
    @Autowired
    private ProductRepository repository;

    private Product mockProduct1;
    private Product mockProduct2;

    @BeforeEach
    void setUp() {
        this.mockProduct1 = Product.builder()
                .sellerId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .productPrice(10000)
                .productStock(100)
                .productName("Product One")
                .productDescription("Product One")
                .productAiSummary("Product One")
                .productStatus("PENDING")
                .build();
        this.mockProduct2 = Product.builder()
                .sellerId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .productPrice(20000)
                .productStock(200)
                .productName("Product Two")
                .productDescription("Product Two")
                .productAiSummary("Product Two")
                .productStatus("PENDING")
                .build();
        this.repository.save(this.mockProduct1);
        this.repository.save(this.mockProduct2);
    }

    @Test
    @DisplayName("승인 대기중인 상품 조회 테스트")
    void testInquirePendingProducts() {
        var pendingProducts = this.service.inquirePendingProducts();

        assertThat(pendingProducts).isNotNull();
        assertThat(pendingProducts.size()).isEqualTo(2);
        assertThat(pendingProducts.getFirst().getProductId())
                .isEqualTo(this.mockProduct1.getProductId());
    }

    @Test
    @DisplayName("상품 승인 처리 테스트")
    void testApproveProduct() {
        this.service.changeProductStatus(
                this.mockProduct1.getProductId(),
                "APPROVED"
        );

        var updatedProduct = this.repository.findById(this.mockProduct1.getProductId())
                .orElse(null);
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getProductStatus())
                .isEqualTo("APPROVED");
    }

    @Test
    @DisplayName("상품 거부 처리 테스트")
    void testRejectProduct() {
        this.service.changeProductStatus(
                this.mockProduct2.getProductId(),
                "REJECTED"
        );

        var updatedProduct = this.repository.findById(this.mockProduct2.getProductId())
                .orElse(null);
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getProductStatus())
                .isEqualTo("REJECTED");
    }
}
