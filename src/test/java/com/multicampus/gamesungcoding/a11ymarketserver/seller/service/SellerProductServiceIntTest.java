package com.multicampus.gamesungcoding.a11ymarketserver.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.S3StorageProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ImageMetadata;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerProductRegisterRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.dto.ProductAnalysisResult;
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service.ProductAnalysisService;
import io.awspring.cloud.s3.S3Template;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SellerProductServiceIntTest {

    @Autowired
    private SellerService sellerService;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserRepository userRepository;

    // 외부 의존성은 Mocking (실제 MinIO/OpenAI 호출 방지)
    @MockitoBean
    private S3Template s3Template;
    @MockitoBean
    private ProductAnalysisService productAnalysisService;
    @MockitoBean
    private S3StorageProperties s3StorageProperties;

    private final String userEmail = "seller@example.com";
    private UUID categoryId;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        var savedUser = userRepository.save(
                Users.builder()
                        .userEmail(userEmail)
                        .userName("Test Seller")
                        .build());

        var seller = Seller.builder()
                .user(savedUser)
                .sellerName(savedUser.getUserName())
                .businessNumber("123-45-67890")
                .sellerIntro("Hello, we are a11y market sellers.")
                .build();
        seller.approve();

        sellerRepository.save(seller);

        var category = categoryRepository.save(
                Categories.builder()
                        .categoryName("Test Category")
                        .build()
        );

        categoryId = category.getCategoryId();

        Mockito.when(s3StorageProperties.getBucket()).thenReturn("test-bucket");

        Mockito.when(productAnalysisService.analysisProductImage(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ProductAnalysisResult("AI Summary", "Context", "Usage"));
    }

    @Test
    @DisplayName("상품 저장 및 메타데이터 처리 통합 테스트")
    void registerProduct_Integration_Success() {
        // given
        var request = new SellerProductRegisterRequest(
                "Integration Product", "DB Test Desc", this.categoryId.toString(), 50000, 100,
                List.of(new ImageMetadata("real.jpg", "alt text", 1, null, null))
        );

        var image = new MockMultipartFile("images", "real.jpg", "image/png", "binary".getBytes());

        var response = sellerService.registerProduct(userEmail, request, List.of(image));

        Assertions.assertThat(response.productName()).isEqualTo("Integration Product");

        Mockito.verify(s3Template).upload(ArgumentMatchers.eq("test-bucket"),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(InputStream.class));

        Assertions.assertThat(response.productId()).isNotNull();
    }
}
