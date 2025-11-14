package com.multicampus.gamesungcoding.a11ymarketserver.admin.product.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.product.service.AdminProductManageService;
import com.multicampus.gamesungcoding.a11ymarketserver.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(AdminProductManageController.class)
class ProductManageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AdminProductManageService service;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("등록 대기 상품 조회 테스트")
    void testInquirePendingProducts() throws Exception {
        BDDMockito.given(this.service.inquirePendingProducts()).willReturn(List.of());

        this.mockMvc.perform(get("/api/v1/admin/products/pending"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("상품 상태 변경 테스트")
    void testChangeProductStatus() throws Exception {
        String mockProductId = "123e4567-e89b-12d3-a456-426614174000";
        String mockStatus = "APPROVED";

        BDDMockito.willDoNothing()
                .given(this.service)
                .changeProductStatus(java.util.UUID.fromString(mockProductId), mockStatus);

        this.mockMvc.perform(patch("/api/v1/admin/products/{productId}/status", mockProductId)
                        .param("status", mockStatus))
                .andExpect(status().isOk());
    }
}
