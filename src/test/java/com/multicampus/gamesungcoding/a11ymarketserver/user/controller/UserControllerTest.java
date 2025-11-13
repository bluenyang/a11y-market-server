package com.multicampus.gamesungcoding.a11ymarketserver.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.config.SecurityConfig;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @WithMockUser
    @Test
    void testGetUserEndpoint() throws Exception {
        String mockUserId = "019A698A-43EA-7785-87A6-4BA7E9E58784";
        String mockEmail = "user1@example.com";
        UserResponse mockResponse = UserResponse.builder()
                .userId(UUID.fromString(mockUserId))
                .userEmail(mockEmail)
                .build();

        given(userService.getUserInfo(UUID.fromString(mockUserId))).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/users/me")
                        .sessionAttr("userId", "019A698A-43EA-7785-87A6-4BA7E9E58784"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value(mockEmail));
    }
}
