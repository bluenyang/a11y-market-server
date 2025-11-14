package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ProductManageControllerIntTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;


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
        this.productRepository.save(this.mockProduct1);
        this.productRepository.save(this.mockProduct2);
    }

    @Test
    @DisplayName("등록 대기 상품 조회 통합 테스트")
    void testInquirePendingProducts() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/admin/products/pending"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 상태 변경 통합 테스트 - 승인")
    void testChangeProductStatusApprove() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/admin/products/{productId}/status", this.mockProduct1.getProductId())
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("SUCCESS"));

        var updatedProduct = this.productRepository
                .findById(this.mockProduct1.getProductId())
                .orElseThrow();
        assertThat(updatedProduct.getProductStatus().equals("APPROVED"));
    }

    @Test
    @DisplayName("상품 상태 변경 통합 테스트 - 거부")
    void testChangeProductStatusReject() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/admin/products/{productId}/status", this.mockProduct2.getProductId())
                        .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("SUCCESS"));

        var updatedProduct = this.productRepository
                .findById(this.mockProduct2.getProductId())
                .orElseThrow();
        assertThat(updatedProduct.getProductStatus().equals("REJECTED"));
    }
}
