package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductManageServiceTest {
    @InjectMocks
    private AdminProductManageService service;

    @Mock
    private ProductRepository repository;

    private final UUID mockPrdId1 = UUID.randomUUID();
    private Product mockProduct1;
    private Product mockProduct2;

    // setup method
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
    }

    // inquire Pending Products

    @Test
    @DisplayName("등록 대기중인 상품 조회 테스트")
    @SuppressWarnings("unchecked")
    void testInquirePendingProducts() {
        BDDMockito.given(this.repository.findAll(any(Specification.class)))
                .willReturn(List.of(this.mockProduct1, this.mockProduct2));

        var result = this.service.inquirePendingProducts();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    // change Product Status
    @Test
    @DisplayName("상품 상태 변경 테스트")
    void testChangeProductStatus() {
        String newStatus = "APPROVED";
        BDDMockito.given(this.repository.findById(this.mockPrdId1))
                .willReturn(java.util.Optional.of(this.mockProduct1));

        this.service.changeProductStatus(this.mockPrdId1, newStatus);

        assertThat(this.mockProduct1.getProductStatus())
                .isEqualTo(newStatus);
    }
}
