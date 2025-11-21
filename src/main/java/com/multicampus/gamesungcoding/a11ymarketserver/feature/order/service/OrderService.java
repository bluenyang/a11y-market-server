package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartItemRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCheckRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCheckoutResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderCreateRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.OrderResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// 결제 정보 조회
@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;

    // 결제 정보 조회
    @Transactional(readOnly = true)
    public OrderCheckoutResponse getCheckoutInfo(String userEmail, OrderCheckRequest req) {
        List<CartItems> cartItems = this.getCartItemsByIds(userEmail, req.checkoutItemIds());

        int totalAmount = 0;
        for (CartItems item : cartItems) {
            Product p = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalStateException("상품 정보를 찾을 수 없습니다."));
            int qty = item.getQuantity();
            int subtotal = p.getProductPrice() * qty;
            totalAmount += subtotal;
        }

        int shippingFee = 0;

        // 최종 반환
        return new OrderCheckoutResponse(
                OrderCheckoutStatus.AVAILABLE,
                totalAmount,
                shippingFee,
                totalAmount + shippingFee
        );
    }

    // 주문 생성
    @Transactional
    public OrderResponse createOrder(String userEmail, OrderCreateRequest req) {
        Users user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        Addresses address = addressRepository.findById(UUID.fromString(req.addressId()))
                .orElseThrow(() -> new DataNotFoundException("주소를 찾을 수 없습니다."));

        Orders order = ordersRepository.save(Orders.builder()
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .userPhone(user.getUserPhone())
                .receiverName(address.getAddress().getReceiverName())
                .receiverPhone(address.getAddress().getReceiverPhone())
                .receiverZipcode(address.getAddress().getReceiverZipcode())
                .receiverAddr1(address.getAddress().getReceiverAddr1())
                .receiverAddr2(address.getAddress().getReceiverAddr2())
                .totalPrice(0) // 초기값 설정, 실제 가격은 추후 계산
                .orderStatus(OrderStatus.PENDING)
                .build());

        var cartItems = this.getCartItemsByIds(userEmail, req.orderItemIds());
        int totalAmount = 0;
        for (var cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("상품을 찾을 수 없습니다."));
            int quantity = cartItem.getQuantity();
            int price = product.getProductPrice();

            // 주문 아이템 생성
            orderItemsRepository.save(
                    OrderItems.builder()
                            .orderId(order.getOrderId())
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .productPrice(price)
                            .productQuantity(quantity)
                            .orderItemStatus(OrderItemStatus.ORDERED)
                            .build()
            );

            // 총 금액 계산
            totalAmount += price * quantity;
        }

        order.updateTotalPrice(totalAmount);

        // 주문 정보를 저장하면서 CartItems를 정리하지 않는 이유:
        // 아직 결제가 완료되지 않은 상태이므로, 사용자가 결제를 취소하거나 수정할 수 있기 때문
        // 결제가 완료된 후에 별도의 프로세스를 통해 CartItems를 정리하는 것이 일반적임
        // 결제 이전에 취소하면, CartItems는 여전히 유효하며, 사용자는 다시 결제를 시도할 수 있음
        return OrderResponse.fromEntity(ordersRepository.save(order));
    }

    private List<CartItems> getCartItemsByIds(String userEmail, List<String> orderItemIds) {
        List<UUID> itemUuids = orderItemIds.stream()
                .map(UUID::fromString)
                .toList();

        List<CartItems> cartItems = cartItemRepository.findAllById(itemUuids);
        if (cartItems.size() != itemUuids.size()) {
            throw new InvalidRequestException("일부 장바구니 상품을 찾을 수 없습니다.");
        }

        var cartList = cartRepository.findAllById(
                // CartItemId로 각 Item를 추가한 Cart 조회
                cartItems.stream()
                        .map(CartItems::getCartId)
                        .toList()
        );

        // 소유자 검증
        var emailList =
                // Id
                userRepository.findAllById(
                                cartList.stream()
                                        .map(Cart::getUserId)
                                        .toList()
                        )
                        .stream()
                        // userEmail 추출
                        .map(Users::getUserEmail)
                        // 중복 제거
                        .distinct()
                        .toList();

        // 소유자가 요청자와 일치하는지 확인
        if (!(emailList.size() == 1 && emailList.getFirst().equals(userEmail))) {
            throw new InvalidRequestException("장바구니 아이템의 소유자와 요청자가 일치하지 않습니다.");
        }

        return cartItems;
    }
}
