package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemDto;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartItemRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
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
@Transactional(readOnly = true)
public class OrderService {
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;
    private final TossPaymentService tossPaymentService;

    public OrderSheetResponse getOrderSheet(String userEmail, OrderSheetRequest req) {
        List<CartItemDto> orderItems = new ArrayList<>();

        if (req.isFromCart()) {
            var itemIds = req.getCartItemIds()
                    .stream()
                    .map(UUID::fromString)
                    .toList();

            var cartItems = cartItemRepository.findAllById(itemIds);
            for (var item : cartItems) {
                if (item.getProduct().getProductStock() < item.getQuantity()) {
                    throw new InvalidRequestException("재고가 부족한 상품이 포함되어 있습니다.");
                }

                if (item.getProduct().getProductStatus() != ProductStatus.APPROVED) {
                    throw new InvalidRequestException("구매할 수 없는 상품이 포함되어 있습니다.");
                }

                orderItems.add(CartItemDto.fromEntity(item));
            }
        } else {
            var orderItemReq = req.getDirectOrderItem();
            var product = productRepository.findById(UUID.fromString(orderItemReq.productId()))
                    .orElseThrow(() -> new DataNotFoundException("상품을 찾을 수 없습니다."));

            if (product.getProductStatus() != ProductStatus.APPROVED) {
                throw new InvalidRequestException("구매할 수 없는 상품입니다.");
            }

            if (product.getProductStock() < orderItemReq.quantity()) {
                throw new InvalidRequestException("재고가 부족한 상품입니다.");
            }
            orderItems.add(CartItemDto.of(product, orderItemReq.quantity()));
        }

        int totalAmount = orderItems.stream()
                .mapToInt(item -> item.productPrice() * item.quantity())
                .sum();

        int shippingFee = 0;

        // 사용가능한 주소 조회
        List<Addresses> addresses = addressRepository.findAllByUser_UserEmail(userEmail);
        if (addresses.isEmpty()) {
            throw new DataNotFoundException("사용 가능한 배송지가 없습니다.");
        }

        return new OrderSheetResponse(
                orderItems,
                totalAmount,
                shippingFee,
                totalAmount + shippingFee
        );
    }

    // 주문 생성
    @Transactional
    public OrderResponse createOrder(String userEmail, OrderCreateRequest req) {
        Addresses address = addressRepository.findByAddressIdAndUser_UserEmail(
                        UUID.fromString(req.addressId()),
                        userEmail)
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

        List<OrderItems> orderItemsList = new ArrayList<>();
        int totalAmount = 0;

        if (req.isFromCart()) {
            var cartItems = this.getCartItemsByIds(userEmail, req.cartItemIds());

            for (var cartItem : cartItems) {
                this.validateProduct(cartItem.getProduct(), cartItem.getQuantity());

                OrderItems item = createOrderItemFromProduct(order, cartItem.getProduct(), cartItem.getQuantity());
                orderItemsList.add(item);
                totalAmount += item.getProductPrice() * item.getProductQuantity();
            }
        } else if (req.directOrderItem() != null) {
            var directItemReq = req.directOrderItem();

            Product product = productRepository.findById(UUID.fromString(directItemReq.productId()))
                    .orElseThrow(() -> new DataNotFoundException("상품을 찾을 수 없습니다."));

            this.validateProduct(product, directItemReq.quantity());

            OrderItems item = createOrderItemFromProduct(order, product, directItemReq.quantity());
            orderItemsList.add(item);
            totalAmount += item.getProductPrice() * item.getProductQuantity();
        } else {
            throw new InvalidRequestException("주문할 아이템이 없습니다.");
        }

        orderItemsRepository.saveAll(orderItemsList);
        order.updateTotalPrice(totalAmount);

        return OrderResponse.fromEntity(order);
    }

    // 내 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(String userEmail) {
        return ordersRepository.findAllByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(OrderResponse::fromEntity)
                .toList();
    }

    // 내 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderDetailResponse getMyOrderDetail(UUID orderItemId, String userEmail) {
        var orderItem = orderItemsRepository
                .findById(orderItemId)
                .orElseThrow(() -> new DataNotFoundException("주문 상품을 찾을 수 없습니다."));

        if (!orderItem.getOrder().getUserEmail().equals(userEmail)) {
            throw new InvalidRequestException("해당 주문 상품에 대한 권한이 없습니다.");
        }

        return OrderDetailResponse.fromEntity(orderItem);
    }

    @Transactional
    public void cancelOrderItems(String userEmail, OrderCancelRequest req) {
        // 권한 검증
        OrderItems orderItem = orderItemsRepository
                .findById(UUID.fromString(req.orderItemId()))
                .orElseThrow(() -> new DataNotFoundException("주문 상품을 찾을 수 없습니다."));

        Orders order = ordersRepository
                .findByOrderIdAndUserEmail(orderItem.getOrder().getOrderId(), userEmail)
                .orElseThrow(() -> new InvalidRequestException("해당 주문 상품에 대한 권한이 없습니다."));

        switch (orderItem.getOrderItemStatus()) {
            case ORDERED, PAID -> {
                tossPaymentService.cancelPayment(
                        order.getPaymentKey(),
                        req.reason(),
                        orderItem.getProductPrice() * orderItem.getProductQuantity()
                );

                orderItem.cancelOrderItem(req.reason());
                orderItem.getProduct().fillUpStock(orderItem.getProductQuantity());
            }
            case ACCEPTED, SHIPPED -> orderItem.cancelOrderItem(req.reason());
            default -> throw new InvalidRequestException("취소할 수 없는 주문 상태입니다.");
        }

        // 주문 취소 처리
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

            // 재고 차감
            item.getProduct().fillUpStock(-item.getProductQuantity());
        }

        tossPaymentService.confirmPayment(req.paymentKey(), req.orderId(), req.amount());
        // 주문 paymentKey 저장
        order.updatePaymentKey(req.paymentKey());

        if (req.cartItemIdsToDelete() != null && !req.cartItemIdsToDelete().isEmpty()) {
            List<UUID> cartUuids = req.cartItemIdsToDelete()
                    .stream()
                    .map(UUID::fromString)
                    .toList();
            cartItemRepository.deleteAllByIdInBatch(cartUuids);
        }

        return PaymentVerifyResponse.success(order.getOrderId(), expectedAmount);
    }

    // Helper methods

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

    private void validateProduct(Product product, int quantity) {
        if (product.getProductStatus() != ProductStatus.APPROVED) {
            throw new InvalidRequestException("구매할 수 없는 상품이 포함되어 있습니다.");
        }

        if (product.getProductStock() < quantity) {
            throw new InvalidRequestException("재고가 부족한 상품이 포함되어 있습니다.");
        }
    }

    private OrderItems createOrderItemFromProduct(Orders order, Product product, int quantity) {
        String imageUrl = product.getProductImages().isEmpty()
                ? null
                : product.getProductImages().getFirst().getImageUrl();

        return OrderItems.builder()
                .order(order)
                .product(product)
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productQuantity(quantity)
                .productImageUrl(imageUrl)
                .build();
    }
}
