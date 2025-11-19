package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserAdminDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AdminUserManageService {
    private final UserRepository userRepository;

    public List<UserAdminDTO> listAll() {
        return userRepository.findAll()
                .stream()
                .map(UserAdminDTO::fromEntity)
                .toList();
    }

    public String changePermission(UUID userId, String role) {
        AtomicReference<String> response = new AtomicReference<>("FAILURE");
        userRepository.findById(userId).ifPresent(user -> {
            user.changeRole(role);
            userRepository.save(user);
            response.set("SUCCESS");
        });
        return response.get();
    }

}
