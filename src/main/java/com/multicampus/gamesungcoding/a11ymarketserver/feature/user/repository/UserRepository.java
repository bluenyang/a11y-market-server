package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUserEmail(
            @NotBlank(message = "이메일은 필수 입력 값입니다.")
            @Email(message = "형식에 맞는 이메일 주소여야 합니다.")
            String email
    );


    boolean existsByUserEmail(
            @NotBlank(message = "이메일은 필수 입력 값입니다.")
            @Email(message = "형식에 맞는 이메일 주소여야 합니다.")
            String email
    );
}
