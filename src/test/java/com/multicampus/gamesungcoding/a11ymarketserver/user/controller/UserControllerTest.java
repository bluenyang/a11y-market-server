package com.multicampus.gamesungcoding.a11ymarketserver.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicampus.gamesungcoding.a11ymarketserver.common.config.SecurityConfig;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.common.oauth.OAuth2LoginSuccessHandler;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.CorsProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.controller.UserController;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserDeleteRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
@EnableConfigurationProperties(CorsProperties.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Test
    @WithMockUser(username = "user1@example.com")
    @DisplayName("유저 정보 조회 - 성공")
    void testGetUserEndpoint() throws Exception {
        String mockEmail = "user1@example.com";
        UserResponse mockResponse = new UserResponse(
                null,
                null,
                mockEmail,
                null,
                null,
                null,
                null,
                null);

        given(userService.getUserInfo(mockEmail)).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value(mockEmail));
    }

    @Test
    @DisplayName("회원 탈퇴: 일반 유저 - 성공")
    @WithMockUser(username = "user1@example.com", roles = "USER")
    void testDeleteUser_AsNormalUser_Success() throws Exception {
        var deleteReq = new UserDeleteRequest("password1234");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/me")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(deleteReq))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
