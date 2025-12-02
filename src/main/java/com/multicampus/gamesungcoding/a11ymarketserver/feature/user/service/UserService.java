package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service.SellerService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserDeleteRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserUpdateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;
    private final SellerService sellerService;

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

    @Transactional
    public void deleteUser(String userEmail, UserDeleteRequest req) {
        var user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        if (!passwordEncoder.matches(req.userPassword(), user.getUserPass())) {
            throw new InvalidRequestException("비밀번호가 일치하지 않습니다.");
        }

        if (user.getUserRole().equals(UserRole.SELLER)) {
            // 판매자 회원의 경우 진행 중인 주문이 있는지 확인
            if (orderItemsRepository.existsByProduct_Seller_User_UserEmail_AndOrderItemStatusIn(
                    userEmail,
                    OrderItemStatus.inProgressStatuses())) {
                throw new InvalidRequestException("진행 중인 주문이 있어 회원 탈퇴가 불가능합니다.");
            }

            // 모든 Product를 논리적으로 삭제 처리
            sellerService.deleteProducts(
                    userEmail,
                    productRepository.findAllBySeller_User_UserEmail(userEmail));
        }

        log.info("Deleting user with email: {}", userEmail);

        authService.logout(userEmail);

        userRepository.delete(user);
    }
}
