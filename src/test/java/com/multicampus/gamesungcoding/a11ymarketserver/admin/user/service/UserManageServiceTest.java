package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class UserManageServiceTest {
    @InjectMocks
    private AdminUserManageService service;

    @Mock
    private UserRepository userRepository;

    private final UUID mockUser1Id = UUID.randomUUID();
    private Users mockUser1;
    private Users mockUser2;


    @BeforeEach
    void setUp() {
        this.mockUser1 = Users.builder()
                .userId(this.mockUser1Id)
                .userEmail("user1@example.com")
                .userPass("encodedPassword1")
                .userName("User One")
                .userNickname("user-one")
                .userPhone("01012345678")
                .userRole("USER")
                .build();
        this.mockUser2 = Users.builder()
                .userId(UUID.randomUUID())
                .userEmail("user2@example.com")
                .userPass("encodedPassword2")
                .userName("User Two")
                .userNickname("user-two")
                .userPhone("01023456789")
                .userRole("USER")
                .build();
    }

    @Test
    @DisplayName("전체 사용자 조회 테스트")
    void testInquireUsers() {
        BDDMockito.given(this.userRepository.findAll())
                .willReturn(List.of(this.mockUser1, this.mockUser2));

        var users = this.service.listAll();

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("사용자 권한 변경 테스트")
    void testChangeUserPermission() {
        String newRole = "ADMIN";
        BDDMockito.given(this.userRepository.findById(this.mockUser1Id))
                .willReturn(Optional.of(this.mockUser1));

        UserResponse result = this.service.changePermission(this.mockUser1Id, newRole);

        assertThat(result.getUserRole()).isEqualTo(newRole);
        assertThat(this.mockUser1.getUserRole()).isEqualTo(newRole);
    }
}
