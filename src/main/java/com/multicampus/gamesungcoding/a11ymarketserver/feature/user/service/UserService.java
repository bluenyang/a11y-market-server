package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.UserUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 마이페이지 - 회원 정보 조회
    public UserResponse getUserInfo(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));
    }

    // 마이페이지 - 회원 정보 수정
    @Transactional
    public UserResponse updateUserInfo(String userEmail, UserUpdateRequest dto) {
        return userRepository.findByUserEmail(userEmail)
                .map(user -> {
                    user.updateUserInfo(dto);
                    return user;
                })
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));
    }

}
