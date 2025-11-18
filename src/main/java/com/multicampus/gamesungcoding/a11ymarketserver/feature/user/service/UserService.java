package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 마이페이지 - 회원 정보 조회
    public UserResponse getUserInfo(UUID userId) {
        return userRepository.findById(userId)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("User not no found : " + userId));
    }

    // 마이페이지 - 회원 정보 수정
    @Transactional
    public UserResponse updateUserInfo(UUID userId, UserUpdateRequest dto) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.updateUserInfo(dto);
                    return user;
                })
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("User not no found : " + userId));
    }

}
