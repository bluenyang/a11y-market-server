package com.multicampus.gamesungcoding.a11ymarketserver.auth.service;

import com.multicampus.gamesungcoding.a11ymarketserver.auth.dto.JoinRequestDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.auth.dto.LoginDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.auth.dto.UserRespDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRespDTO login(LoginDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        var optionalUser = userRepository.findByUserEmail(email);

        if (optionalUser.isEmpty()) {
            return null;
        }

        Users user = optionalUser.get();


        if (passwordEncoder.matches(password, user.getUserPass())) {
            return UserRespDTO.fromEntity(user);
        }

        return null;
    }

    public UserRespDTO join(JoinRequestDTO dto) {
        // 이메일 중복 체크
        if (userRepository.existsByUserEmail(dto.getEmail())) {
            return null;
        }
        // 비밀번호 암호화
        String encodedPwd = passwordEncoder.encode(dto.getPassword());

        // UUID 생성
        // UUID userId = UUID.randomUUID();

        Users user = Users.builder()
                .userEmail(dto.getEmail())
                .userPass(encodedPwd)
                .userName(dto.getUsername())
                .userNickname(dto.getNickname())
                .userPhone(dto.getPhone())
                .userRole("USER")
                .build();

        // 저장 후 반환
        return UserRespDTO.fromEntity(userRepository.save(user));
    }

}
