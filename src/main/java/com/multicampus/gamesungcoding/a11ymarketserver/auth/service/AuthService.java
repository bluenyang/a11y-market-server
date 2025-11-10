package com.multicampus.gamesungcoding.a11ymarketserver.auth.service;

import com.multicampus.gamesungcoding.a11ymarketserver.auth.dto.LoginDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

}
