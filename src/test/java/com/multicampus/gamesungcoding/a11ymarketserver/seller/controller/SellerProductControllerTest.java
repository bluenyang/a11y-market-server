package com.multicampus.gamesungcoding.a11ymarketserver.seller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.CorsProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.controller.SellerController;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.SellerProductRegisterRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerDashboardService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@Import(SellerController.class)
@WebMvcTest(SellerController.class)
@EnableConfigurationProperties(CorsProperties.class)
public class SellerProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoBean
    private SellerService sellerService; // Service Mocking
    @MockitoBean
    private SellerDashboardService sellerDashboardService;

    @Test
    @DisplayName("상품 등록 API 성공 테스트")
    @WithMockUser(username = "seller@test.com", roles = "SELLER")
    void registerProductSuccess() throws Exception {
        SellerProductRegisterRequest requestDto = new SellerProductRegisterRequest(
                "New Product", "Desc", UUID.randomUUID().toString(), 1000, 5, List.of()
        );

        // JSON Part 생성
        MockMultipartFile dataPart = new MockMultipartFile(
                "data",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        MockMultipartFile imagePart = new MockMultipartFile(
                "images",
                "test.jpg",
                "image/jpeg",
                "dummy".getBytes()
        );

        // Service Stubbing
        var respDto = new ProductDetailResponse(
                UUID.randomUUID(),
                "New Product",
                UUID.randomUUID(),
                "Seller Name",
                SellerGrades.NEWER,
                true,
                0,
                ProductStatus.PENDING,
                "Desc",
                0,
                List.of(),
                UUID.randomUUID(),
                "Category Name",
                "summary",
                "context",
                "method of use");
        BDDMockito.when(sellerService.registerProduct(BDDMockito.anyString(), BDDMockito.any(), BDDMockito.any()))
                .thenReturn(respDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/seller/products")
                        .file(dataPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName")
                        .value("New Product"));
    }
}
