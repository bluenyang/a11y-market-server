package com.multicampus.gamesungcoding.a11ymarketserver.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.S3StorageProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ImageMetadata;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductAiSummaryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductImagesRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerProductRegisterRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.dto.ProductAnalysisResult;
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service.ProductAnalysisService;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SellerProductServiceTest {
    @InjectMocks
    private SellerService sellerService;

    @Mock
    private ProductAnalysisService productAnalysisService;
    @Mock
    private S3Template s3Template;
    @Mock
    private S3StorageProperties s3StorageProperties;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductImagesRepository productImagesRepository;
    @Mock
    private ProductAiSummaryRepository productAiSummaryRepository;

    @Test
    @DisplayName("상품 등록 성공 - 이미지 및 AI 요약 포함")
    void registerProductSuccess() {
        String email = "seller@example.com";
        String categoryId = UUID.randomUUID().toString();

        var req = new SellerProductRegisterRequest(
                "Test Product",
                "This is a test product.",
                categoryId,
                10000,
                50,
                List.of(
                        new ImageMetadata(
                                "test.jpg",
                                "alt",
                                0,
                                null,
                                null)
                )
        );

        var image = new MockMultipartFile(
                "images",
                "test.jpg",
                "image/jpeg",
                "content".getBytes());

        // Mock seller approval
        var mockUser = Users.builder()
                .userEmail(email)
                .build();

        var seller = Seller.builder()
                .user(mockUser)
                .build();
        seller.approve();

        BDDMockito.when(sellerRepository.findByUser_UserEmail(email))
                .thenReturn(Optional.of(seller));

        var category = Categories.builder()
                .categoryName("Test Category")
                .build();
        var product = Product.builder()
                .seller(seller)
                .category(category)
                .build();
        BDDMockito.when(productRepository.save(
                        BDDMockito.any(Product.class)))
                .thenReturn(product);

        var productImage = ProductImages.builder()
                .product(product)
                .imageUrl("https://example.com/test.jpg")
                .altText("alt")
                .imageSequence(0)
                .build();
        BDDMockito.when(productImagesRepository.save(
                        BDDMockito.any(ProductImages.class)))
                .thenReturn(productImage);

        BDDMockito.when(s3StorageProperties.getBucket())
                .thenReturn("test-bucket");

        var aiResult = new ProductAnalysisResult(
                "AI Summary",
                "context",
                "how to use...."
        );
        BDDMockito.when(productAnalysisService
                        .analysisProductImage(
                                BDDMockito.any(),
                                BDDMockito.any(),
                                BDDMockito.any()
                        ))
                .thenReturn(aiResult);

        var resp = sellerService.registerProduct(
                email,
                req,
                List.of(image)
        );

        Assertions.assertNotNull(resp);
        Mockito.verify(s3Template, Mockito.times(1))
                .upload(
                        BDDMockito.anyString(),
                        BDDMockito.anyString(),
                        BDDMockito.any(InputStream.class));
        Mockito.verify(productImagesRepository, Mockito.times(1))
                .save(BDDMockito.any());
        Mockito.verify(productAiSummaryRepository, Mockito.times(1))
                .save(BDDMockito.any());
    }

    @Test
    @DisplayName("상품 등록 실패 - 승인되지 않은 판매자")
    void registerProductFail_NotApprovedSeller() {
        String email = "not_appr@example.com";
        var seller = Seller.builder()
                .build();
        BDDMockito.when(sellerRepository.findByUser_UserEmail(email))
                .thenReturn(Optional.of(seller));

        Assertions.assertThrows(InvalidRequestException.class, () ->
                sellerService.registerProduct(
                        email,
                        Mockito.mock(SellerProductRegisterRequest.class),
                        null));
    }
}
