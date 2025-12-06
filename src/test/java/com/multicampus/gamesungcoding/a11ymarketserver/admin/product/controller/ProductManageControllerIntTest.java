package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;


    private Product mockProduct1;
    private Product mockProduct2;

    @BeforeEach
    void setUp() {
        var mockUser = userRepository.save(
                Users.builder().build()
        );

        var seller = sellerRepository.save(
                Seller.builder()
                        .user(mockUser)
                        .sellerName("Test Seller")
                        .businessNumber("123-45-67890")
                        .build()
        );

        var category = categoryRepository.save(
                Categories.builder()
                        .categoryName("Test Category")
                        .build()
        );

        this.mockProduct1 = Product.builder()
                .seller(seller)
                .category(category)
                .productPrice(10000)
                .productStock(100)
                .productName("Product One")
                .productDescription("Product One")
                // .productAiSummary("Product One")
                .productStatus(ProductStatus.PENDING)
                .build();
        this.mockProduct2 = Product.builder()
                .seller(seller)
                .category(category)
                .productPrice(20000)
                .productStock(200)
                .productName("Product Two")
                .productDescription("Product Two")
                // .productAiSummary("Product Two")
                .productStatus(ProductStatus.PENDING)
                .build();
        this.productRepository.save(this.mockProduct1);
        this.productRepository.save(this.mockProduct2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("등록 대기 상품 조회 통합 테스트")
    void testInquirePendingProducts() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/admin/products/pending"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("상품 상태 변경 통합 테스트 - 승인")
    void testChangeProductStatusApprove() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/admin/products/{productId}/status", this.mockProduct1.getProductId())
                        .param("status", ProductStatus.APPROVED.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("SUCCESS"));

        var updatedProduct = this.productRepository
                .findById(this.mockProduct1.getProductId())
                .orElseThrow();
        assertThat(updatedProduct.getProductStatus().equals(ProductStatus.APPROVED));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
        assertThat(updatedProduct.getProductStatus().equals(ProductStatus.REJECTED));
    }
}
