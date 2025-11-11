package com.multicampus.gamesungcoding.a11ymarketserver.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.user.model.User;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.UserRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.user.repository.UserRepository;
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
        User user = userRepository.findById(userId)
                // TODO: 추후 예외 처리 핸들러 추가 후 처리
                .orElseThrow(() -> new IllegalArgumentException("User not no found : " + userId));
        return UserResponse.from(user);

    }

    // 마이페이지 - 회원 정보 수정
    @Transactional
    public UserResponse updateUserInfo(UUID userId, UserRequest request) {
        User user = userRepository.findById(userId)
                // TODO: 추후 예외 처리 핸들러 추가 후 처리
                .orElseThrow(() -> new IllegalArgumentException("User not no found : " + userId));
        user.updateUserInfo(request);
        return UserResponse.from(user);
    }

}
