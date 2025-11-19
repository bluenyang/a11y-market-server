package com.multicampus.gamesungcoding.a11ymarketserver.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.common.config.SecurityConfig;
import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.controller.UserController;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(username = "user1@example.com")
    void testGetUserEndpoint() throws Exception {
        String mockEmail = "user1@example.com";
        UserResponse mockResponse = UserResponse.builder()
                .userEmail(mockEmail)
                .build();

        given(userService.getUserInfo(mockEmail)).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value(mockEmail));
    }
}
