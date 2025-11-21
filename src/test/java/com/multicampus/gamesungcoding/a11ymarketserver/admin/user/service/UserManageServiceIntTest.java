package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class UserManageServiceIntTest {
    @Autowired
    private AdminUserManageService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UUID userId1;

    @BeforeEach
    void setUp() {
        this.userRepository.save(Users.builder()
                .userEmail("user1@example.com")
                .userPass(this.passwordEncoder.encode("password123!"))
                .userName("User One")
                .userNickname("user-one")
                .userPhone("01012345678")
                .userRole("USER")
                .build());

        this.userRepository.save(Users.builder()
                .userEmail("user2@example.com")
                .userPass(this.passwordEncoder.encode("password123!"))
                .userName("User Two")
                .userNickname("user-two")
                .userPhone("01023456789")
                .userRole("USER")
                .build());

        var user1 = this.userRepository.findByUserEmail("user1@example.com")
                .orElseThrow();
        this.userId1 = user1.getUserId();
    }

    @Test
    @DisplayName("모든 사용자 조회 통합 테스트")
    void listAllUsersIntegrationTest() {
        var users = this.service.listAll();
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("사용자 권한 변경 통합 테스트")
    void changeUserRoleIntegrationTest() {
        var result = this.service.changePermission(this.userId1, "ADMIN");
        assertThat(result.getUserRole()).isEqualTo("ADMIN");

        var updatedUser = this.userRepository.findById(this.userId1).orElseThrow();
        assertThat(updatedUser.getUserRole()).isEqualTo("ADMIN");
    }
}
