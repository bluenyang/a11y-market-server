package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartItemRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductImagesRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 결제 정보 조회
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductImagesRepository productImagesRepository;

    // 결제 정보 조회
    @Transactional(readOnly = true)
    public OrderCheckoutResponse getCheckoutInfo(String userEmail, OrderCheckRequest req) {
        if (req.orderAllItems() == false && (req.checkoutItemIds() == null || req.checkoutItemIds().isEmpty())) {
            throw new InvalidRequestException("결제할 장바구니 아이템이 없습니다.");
        }

        List<CartItems> cartItems = req.orderAllItems() ?
                cartItemRepository.findByCart_User_UserEmail(userEmail) :
                this.getCartItemsByIds(userEmail, req.checkoutItemIds());

        int totalAmount = 0;
        for (CartItems item : cartItems) {
            Product product = item.getProduct();
            if (product == null) {
                throw new DataNotFoundException("장바구니에 담긴 상품을 찾을 수 없습니다.");
            }

            int qty = item.getQuantity();
            int subtotal = product.getProductPrice() * qty;
            totalAmount += subtotal;
        }

        int shippingFee = 0;

        // 사용가능한 주소 조회
        List<Addresses> addresses = addressRepository.findAllByUser_UserEmail(userEmail);
        if (addresses.isEmpty()) {
            throw new DataNotFoundException("사용 가능한 배송지가 없습니다.");
        }

        var defaultAddress = addresses
                .stream()
                .filter(Addresses::getIsDefault)
                .findFirst()
                .orElse(addresses.getFirst()); // 기본 배송지가 없으면 첫 번째 주소 사용

        // 최종 반환
        return new OrderCheckoutResponse(
                OrderCheckoutStatus.AVAILABLE,
                totalAmount,
                shippingFee,
                totalAmount + shippingFee,
                addresses.stream()
                        .map(AddressResponse::fromEntity)
                        .toList(),
                defaultAddress.getAddressId()
        );
    }

    // 주문 생성
    @Transactional
    public OrderResponse createOrder(String userEmail, OrderCreateRequest req) {
        Addresses address = addressRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("주소를 찾을 수 없습니다."));

        Orders order = ordersRepository.save(Orders.builder()
                .userEmail(address.getUser().getUserEmail())
                .userName(address.getUser().getUserName())
                .userPhone(address.getUser().getUserPhone())
                .receiverName(address.getAddress().getReceiverName())
                .receiverPhone(address.getAddress().getReceiverPhone())
                .receiverZipcode(address.getAddress().getReceiverZipcode())
                .receiverAddr1(address.getAddress().getReceiverAddr1())
                .receiverAddr2(address.getAddress().getReceiverAddr2())
                .totalPrice(0) // 초기값 설정, 실제 가격은 추후 계산
                // .orderStatus(OrderStatus.PENDING)
                .build());

        var cartItems = this.getCartItemsByIds(userEmail, req.orderItemIds());
        int totalAmount = 0;

        for (var cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product == null) {
                throw new DataNotFoundException("장바구니에 담긴 상품을 찾을 수 없습니다.");
            }

            ProductImages images = productImagesRepository.findById(product.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("상품 이미지를 찾을 수 없습니다."));

            int quantity = cartItem.getQuantity();
            int price = product.getProductPrice();

            // 주문 아이템 생성
            orderItemsRepository.save(
                    OrderItems.builder()
                            .order(order)
                            .product(product)
                            .productName(product.getProductName())
                            .productPrice(price)
                            .productQuantity(quantity)
                            .productImageUrl(images.getImageUrl())
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

    // 내 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderDetailResponse> getMyOrders(String userEmail) {
        var orders = ordersRepository
                .findAllByUserEmailOrderByCreatedAtDesc(userEmail);

        List<OrderDetailResponse> result = new ArrayList<>(List.of());

        for (Orders order : orders) {
            List<OrderItems> items = orderItemsRepository.findAllByOrder_OrderId(order.getOrderId());
            result.add(OrderDetailResponse.fromEntity(order, items));
        }

        return result;
    }

    // 내 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderDetailResponse getMyOrderDetail(UUID orderId, String userEmail) {
        Orders order = ordersRepository
                .findByOrderIdAndUserEmail(orderId, userEmail)
                .orElseThrow(() -> new DataNotFoundException("주문을 찾을 수 없습니다."));

        List<OrderItems> items = orderItemsRepository.findAllByOrder_OrderId(order.getOrderId());

        if (items.isEmpty()) {
            throw new DataNotFoundException("주문 아이템이 없습니다.");
        }
        return OrderDetailResponse.fromEntity(order, items);
    }

    @Transactional
    public void cancelOrderItems(String userEmail, UUID orderId, OrderCancelRequest req) {
        // 권한 검증
        Orders order = ordersRepository
                .findByOrderIdAndUserEmail(orderId, userEmail)
                .orElseThrow(() -> new InvalidRequestException("잘못된 요청입니다."));

        var requestedItem = UUID.fromString(req.orderItemId());
        OrderItems orderItem = orderItemsRepository
                .findById(requestedItem)
                .orElseThrow(() -> new DataNotFoundException("주문 상품을 찾을 수 없습니다."));

        if (!orderItem.getOrder().getOrderId().equals(order.getOrderId())) {
            throw new InvalidRequestException("잘못된 요청입니다.");
        }

        // 주문 취소 처리
        orderItem.cancelOrderItem(req.reason());
    }

    // 주문 구매 확정
    @Transactional
    public void confirmOrderItems(String userEmail, String orderId, OrderConfirmRequest req) {

        // orderId 검증
        UUID orderUuid;
        try {
            orderUuid = UUID.fromString(orderId);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("유효하지 않은 주문 ID 입니다.");
        }

        // 본인 주문인지 검증
        Orders order = ordersRepository
                .findByOrderIdAndUserEmail(orderUuid, userEmail)
                .orElseThrow(() -> new DataNotFoundException("주문을 찾을 수 없습니다."));

        UUID itemUuid;

        try {
            itemUuid = UUID.fromString(req.orderItemId());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("유효하지 않은 주문 상품 ID가 포함되어 있습니다.");
        }

        var item = orderItemsRepository.findById(itemUuid)
                .orElseThrow(() -> new DataNotFoundException("주문 상품을 찾을 수 없습니다."));

        if (!item.getOrder().getOrderId().equals(order.getOrderId())) {
            throw new InvalidRequestException("주문 상품이 해당 주문에 포함되어 있지 않습니다.");
        }


        if (item.getOrderItemStatus() == OrderItemStatus.CONFIRMED) {
            throw new InvalidRequestException("이미 구매 확정된 상품이 포함되어 있습니다.");
        }

        if (item.getOrderItemStatus() != OrderItemStatus.SHIPPED) {
            throw new InvalidRequestException("배송 완료된 상품만 구매 확정할 수 있습니다.");
        }

        item.updateOrderItemStatus(OrderItemStatus.CONFIRMED);
        /* orderStatus가 더 이상 사용되지 않으므로 주석 처리
        // 주문 상태 전체 갱신
        var allItems = orderItemsRepository.findAllByOrder_OrderId(orderUuid);

        boolean hasNotFinishedItem = allItems.stream()
                .anyMatch(i -> i.getOrderItemStatus() != OrderItemStatus.CONFIRMED
                        && i.getOrderItemStatus() != OrderItemStatus.CANCELED);

        if (!hasNotFinishedItem) {
            order.updateOrderItemStatus(OrderStatus.SHIPPED);
        }*/
    }

    // 결제 검증
    @Transactional
    public PaymentVerifyResponse verifyPayment(String userEmail, PaymentVerifyRequest req) {

        UUID orderUuid = UUID.fromString(req.orderId());

        Orders order = ordersRepository
                .findByOrderIdAndUserEmail(orderUuid, userEmail)
                .orElseThrow(() -> new DataNotFoundException("주문을 찾을 수 없습니다."));

        // if (order.getOrderStatus() == OrderStatus.PAID) {
        //     throw new InvalidRequestException("이미 결제된 주문입니다.");
        // }

        List<OrderItems> items = orderItemsRepository.findAllByOrder_OrderId(order.getOrderId());

        if (items.isEmpty()) {
            throw new InvalidRequestException("주문 상품이 없습니다.");
        }

        int expectedAmount = items.stream()
                .filter(i -> i.getOrderItemStatus() == OrderItemStatus.ORDERED)
                .mapToInt(i -> i.getProductPrice() * i.getProductQuantity())
                .sum();

        if (expectedAmount != req.amount()) {
            throw new InvalidRequestException("결제 금액이 일치하지 않습니다.");
        }

        for (OrderItems item : items) {
            if (item.getOrderItemStatus() != OrderItemStatus.ORDERED) {
                throw new InvalidRequestException("결제할 수 없는 상품이 포함되어 있습니다.");
            }
            item.updateOrderItemStatus(OrderItemStatus.PAID);
        }

        // order.updateOrderItemStatus(OrderStatus.PAID);

        return PaymentVerifyResponse.success(order.getOrderId(), expectedAmount);
    }

    private List<CartItems> getCartItemsByIds(String userEmail, List<String> orderItemIds) {
        List<UUID> itemUuids;

        try {
            itemUuids = orderItemIds.stream()
                    .map(UUID::fromString)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("유효하지 않은 장바구니 아이템 ID가 포함되어 있습니다.");
        }

        List<CartItems> cartItems = cartItemRepository.findAllByIdWithProductAndImage(itemUuids);
        if (cartItems.size() != itemUuids.size()) {
            log.debug("Requested IDs: {}, Found IDs: {}",
                    itemUuids,
                    cartItems.stream()
                            .map(CartItems::getCartItemId)
                            .toList()
            );
            throw new InvalidRequestException("일부 장바구니 상품을 찾을 수 없습니다.");
        }

        // 소유자 검증
        var emailList = cartItems.stream()
                .map(CartItems::getCart)
                .map(Cart::getUser)
                .map(Users::getUserEmail)
                .distinct()
                .toList();

        // 소유자가 요청자와 일치하는지 확인
        if (!(emailList.size() == 1 && emailList.getFirst().equals(userEmail))) {
            throw new InvalidRequestException("장바구니 아이템의 소유자와 요청자가 일치하지 않습니다.");
        }

        return cartItems;
    }
}
