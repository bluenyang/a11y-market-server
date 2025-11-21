package com.multicampus.gamesungcoding.a11ymarketserver.admin.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserAdminDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserManageService {
    private final UserRepository userRepository;

    public List<UserAdminDTO> listAll() {
        return userRepository.findAll()
                .stream()
                .map(UserAdminDTO::fromEntity)
                .toList();
    }

    @Transactional
    public UserResponse changePermission(UUID userId, String role) {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.changeRole(role);
        return UserResponse.fromEntity(user);
    }

}
