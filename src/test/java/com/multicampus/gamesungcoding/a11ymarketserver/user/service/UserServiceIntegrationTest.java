package com.multicampus.gamesungcoding.a11ymarketserver.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.AddressInfo;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.auth.service.AuthService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSubmitStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto.UserDeleteRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserRole;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceIntegrationTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    @MockitoBean
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Product mockProduct;
    private Orders mockOrder;
    private Users mockUser;
    private Users mockSeller;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성 및 저장
        this.mockUser = this.userRepository.save(
                Users.builder()
                        .userEmail("user1@example.com")
                        .userPass(this.passwordEncoder.encode("password123!"))
                        .userName("User One")
                        .userRole(UserRole.USER)
                        .build());
        this.mockSeller = this.userRepository.save(
                Users.builder()
                        .userEmail("seller@example.com")
                        .userPass(this.passwordEncoder.encode("sellerpass!"))
                        .userName("Seller One")
                        .userRole(UserRole.SELLER)
                        .build());
        var seller = this.sellerRepository.save(
                Seller.builder()
                        .user(this.mockSeller)
                        .sellerName("Seller One")
                        .businessNumber("123-45-67890")
                        .sellerGrade(SellerGrades.NEWER)
                        .a11yGuarantee(true)
                        .sellerSubmitStatus(SellerSubmitStatus.APPROVED)
                        .build()
        );

        // cart 및 address가 있어도 동작하는지 테스트하기 위해 장바구니도 생성
        this.cartRepository.save(
                Cart.builder()
                        .user(this.mockUser)
                        .build()
        );
        var address = this.addressRepository.save(Addresses
                .builder()
                .user(this.mockUser)
                .addressInfo(AddressInfo.builder()
                        .addressName("Home")
                        .receiverName("User One")
                        .receiverPhone("01012345678")
                        .receiverZipcode("12345")
                        .receiverAddr1("123 Main St")
                        .receiverAddr2("Apt 101")
                        .build())
                .build());

        // 판매자의 상품도 생성
        this.mockProduct = this.productRepository.save(
                Product.builder()
                        .seller(seller)
                        .category(this.categoryRepository
                                .getReferenceById(UUID.randomUUID()))
                        .productPrice(25000)
                        .productStock(100)
                        .productName("Test Product")
                        // 판매 승인 상태로 설정
                        .productStatus(ProductStatus.APPROVED)
                        .build());

        // Orders 및 OrderItems도 생성
        this.mockOrder = this.ordersRepository.save(
                Orders.builder()
                        .userName(this.mockUser.getUserName())
                        .userEmail(this.mockUser.getUserEmail())
                        .userPhone("01012345678")
                        .receiverName(address.getAddress().getReceiverName())
                        .receiverPhone(address.getAddress().getReceiverPhone())
                        .receiverZipcode(address.getAddress().getReceiverZipcode())
                        .receiverAddr1(address.getAddress().getReceiverAddr1())
                        .receiverAddr2(address.getAddress().getReceiverAddr2())
                        .totalPrice(50000)
                        .orderStatus(OrderStatus.PENDING)
                        .build()
        );

    }

    @Test
    @DisplayName("회원 탈퇴: 일반 유저 - 성공")
    void deleteUser_AsRegularUser_Success() {
        var orderItem = this.orderItemsRepository.save(
                OrderItems.builder()
                        .order(this.mockOrder)
                        .product(this.mockProduct)
                        .productName(this.mockProduct.getProductName())
                        .productPrice(this.mockProduct.getProductPrice())
                        .productQuantity(2)
                        .build());
        orderItem.updateOrderItemStatus(OrderItemStatus.CONFIRMED);

        var req = new UserDeleteRequest("password123!");
        this.userService.deleteUser(this.mockUser.getUserEmail(), req);

        // 사용자, 장바구니, 주소가 모두 삭제되었는지 확인
        Assertions.assertFalse(this.userRepository
                .findById(this.mockUser.getUserId())
                .isPresent());

        Assertions.assertFalse(this.cartRepository
                .findByUser(this.mockUser)
                .isPresent());

        Assertions.assertEquals(0, this.addressRepository
                .findAllByUser_UserEmail(this.mockUser.getUserEmail())
                .size());

        // 주문 및 주문 상품은 기록이므로, 삭제되지 않아야 함
        Assertions.assertEquals(1, this.ordersRepository.count());
        Assertions.assertEquals(1, this.orderItemsRepository.count());
    }
}
