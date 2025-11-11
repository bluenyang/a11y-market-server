package com.multicampus.gamesungcoding.a11ymarketserver.auth.service;

import com.multicampus.gamesungcoding.a11ymarketserver.auth.dto.JoinRequestDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.auth.dto.LoginDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Users login(LoginDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        var optionalUser = userRepository.findByUserEmail(email);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();


            if (passwordEncoder.matches(password, user.getUserPass())) {
                return user;
            }
        }

        return null;

    }

    public Users join(JoinRequestDTO dto) {
        //이메일 중복 체크
        if (userRepository.existsByUserEmail(dto.getEmail())) {
            return null;
        }
        //비밀번호 암호화
        String encodedPwd = passwordEncoder.encode(dto.getPassword());

        //UUID 생성
        UUID userId = UUID.randomUUID();

        Users user = Users.builder()
                .userId(userId)
                .userEmail(dto.getEmail())
                .userPass(encodedPwd)
                .userName(dto.getUsername())
                .userNickname(dto.getNickname())
                .userPhone(dto.getPhone())
                .userRole("USER")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        //저장 후 반환
        return userRepository.save(user);
    }

}
