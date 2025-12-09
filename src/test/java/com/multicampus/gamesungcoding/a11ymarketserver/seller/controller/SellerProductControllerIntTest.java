package com.multicampus.gamesungcoding.a11ymarketserver.seller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.S3StorageProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ImageMetadata;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerProductRegisterRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.dto.ProductAnalysisResult;
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service.ProductAnalysisService;
import io.awspring.cloud.s3.S3Template;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class SellerProductControllerIntTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoBean
    private S3Template s3Template;
    @MockitoBean
    private ProductAnalysisService productAnalysisService;
    @MockitoBean
    private S3StorageProperties s3StorageProperties;

    private final String userEmail = "seller@example.com";
    private UUID categoryId;

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

        var category = categoryRepository.save(Categories.builder()
                .categoryName("Electronics")
                .build());

        categoryId = category.getCategoryId();

        Mockito.when(s3StorageProperties.getBucket()).thenReturn("test-bucket");
        Mockito.when(productAnalysisService.analysisProductImage(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ProductAnalysisResult("Summary", "Context", "Method"));
    }

    @Test
    @DisplayName("상품 등록 통합 테스트")
    @WithMockUser(username = userEmail, roles = "SELLER")
    void registerProduct_FullFlow() throws Exception {
        // given
        SellerProductRegisterRequest requestDto = new SellerProductRegisterRequest(
                "Full Flow Product",
                "Desc",
                this.categoryId.toString(),
                99000,
                1,
                List.of(
                        new ImageMetadata(
                                "flow.jpg",
                                "alt",
                                0,
                                null,
                                null)
                )
        );

        MockMultipartFile dataPart = new MockMultipartFile(
                "data",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );
        MockMultipartFile imagePart = new MockMultipartFile(
                "images",
                "flow.jpg",
                "image/jpeg",
                "image-data".getBytes()
        );

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/seller/products")
                        .file(dataPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName")
                        .value("Full Flow Product"));
    }
}
