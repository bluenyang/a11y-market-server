package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service.AdminUserManageService;
import com.multicampus.gamesungcoding.a11ymarketserver.common.config.SecurityConfig;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(AdminUserManageController.class)
class UserManageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private AdminUserManageService service;

    @Test
    @WithMockUser
    @DisplayName("전체 사용자 조회 테스트")
    void testInquireUsers() throws Exception {
        this.mockMvc.perform(get("/api/v1/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("사용자 권한 변경 테스트")
    void testChangeUserPermission() throws Exception {
        UUID mockUserId = UUID.randomUUID();
        String mockRole = "USER";

        BDDMockito.given(this.service.changePermission(eq(mockUserId), anyString()))
                .willReturn(
                        UserResponse.builder()
                                .userId(mockUserId)
                                .userRole(mockRole)
                                .build()
                );

        this.mockMvc.perform(patch("/api/v1/admin/users/{userId}/permission", mockUserId)
                        .param("role", mockRole))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userRole").value("USER"));
    }
}
