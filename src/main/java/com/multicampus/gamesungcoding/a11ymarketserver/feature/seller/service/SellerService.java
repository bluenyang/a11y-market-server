package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;

    public SellerApplyResponse applySeller(String userEmail, SellerApplyRequest request) {
        Users user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        sellerRepository.findByUserId(user.getUserId()).ifPresent(existing -> {
            throw new DataDuplicatedException("이미 판매자이거나 판매자 신청 이력이 존재합니다.");
        });

        Seller seller = Seller.builder()
                .userId(user.getUserId())
                .sellerName(request.sellerName())
                .businessNumber(request.businessNumber())
                .sellerGrade(SellerGrades.NEWER.getGrade())
                .sellerIntro(request.sellerIntro())
                .a11yGuarantee(false)
                .sellerSubmitStatus(SellerSubmitStatus.PENDING.name())
                .build();

        Seller saved = sellerRepository.save(seller);

        return new SellerApplyResponse(
                saved.getSellerId(),
                saved.getSellerName(),
                saved.getBusinessNumber(),
                saved.getSellerGrade(),
                saved.getSellerIntro(),
                saved.getA11yGuarantee(),
                saved.getSellerSubmitStatus(),
                saved.getSubmitDate(),
                saved.getApprovedDate());
    }

    public ProductDTO registerProduct(String userEmail, SellerProductRegisterRequest request) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보가 존재하지 않습니다. 먼저 판매자 가입 신청을 완료하세요."));

        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품 등록이 가능합니다.");
        }

        UUID sellerId = seller.getSellerId();
        UUID categoryId = UUID.fromString(request.categoryId());

        Product product = Product.builder()
                .sellerId(sellerId)
                .categoryId(categoryId)
                .productName(request.productName())
                .productDescription(request.productDescription())
                .productPrice(request.productPrice())
                .productStock(request.productStock())
                .productStatus(ProductStatus.PENDING)
                .build();

        return ProductDTO.fromEntity(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getMyProducts(String userEmail) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        UUID sellerId = seller.getSellerId();

        List<Product> products = productRepository.findBySellerId(sellerId);

        return products.stream().map(ProductDTO::fromEntity).toList();
    }

    @Transactional
    public ProductDTO updateProduct(String userEmail, UUID productId, SellerProductUpdateRequest request) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품을 수정할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품만 수정할 수 있습니다.");
        }

        product.updateBySeller(
                UUID.fromString(request.categoryId()),
                request.productName(),
                request.productDescription(),
                request.productPrice(),
                request.productStock()
        );

        return ProductDTO.fromEntity(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(String userEmail, UUID productId) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품을 삭제할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품만 삭제할 수 있습니다.");
        }

        product.deleteBySeller();

        productRepository.save(product);
    }

    @Transactional
    public ProductDTO updateProductStock(String userEmail, UUID productId, SellerProductStockUpdateRequest request) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 재고를 수정할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품 재고만 수정할 수 있습니다.");
        }

        product.updateStockBySeller(request.productStock());

        return ProductDTO.fromEntity(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<SellerOrderItemResponse> getReceivedOrders(String userEmail, String status) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!SellerSubmitStatus.APPROVED.getStatus().equals(seller.getSellerSubmitStatus())) {
            throw new InvalidRequestException("승인된 판매자만 주문 목록을 조회할 수 있습니다.");
        }

        OrderItemStatus statusFilter = null;
        if (status != null && !status.isBlank()) {
            try {
                statusFilter = OrderItemStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidRequestException("유효하지 않은 주문 상태입니다.");
            }
        }

        return orderItemsRepository.findSellerReceivedOrders(userEmail, statusFilter);
    }

    @Transactional
    public void updateOrderStatus(String userEmail, UUID orderId, SellerOrderStatusUpdateRequest request) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!SellerSubmitStatus.APPROVED.getStatus().equals(seller.getSellerSubmitStatus())) {
            throw new InvalidRequestException("승인된 판매자만 주문 상태를 변경할 수 있습니다.");
        }

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("주문 정보를 찾을 수 없습니다."));

        List<UUID> productIds = productRepository.findBySellerId(seller.getSellerId())
                .stream()
                .map(Product::getProductId)
                .toList();

        if (productIds.isEmpty()) {
            throw new InvalidRequestException("판매자의 상품이 존재하지 않습니다.");
        }

        boolean isMyOrder = orderItemsRepository.existsByOrderIdAndProductIdIn(orderId, productIds);

        if (!isMyOrder) {
            throw new InvalidRequestException("해당 주문에 대한 변경 권한이 없습니다.");
        }

        OrderStatus currentStatus = order.getOrderStatus();
        OrderStatus nextStatus = request.status();

        validateSellerOrderStatusTransition(currentStatus, nextStatus);

        order.updateOrderStatus(request.status());
    }

    private void validateSellerOrderStatusTransition(OrderStatus current, OrderStatus next) {

        if (current == next) {
            throw new InvalidRequestException("이미 동일한 주문 상태입니다.");
        }

        switch (current) {
            case PAID -> {
                if (next != OrderStatus.ACCEPTED && next != OrderStatus.REJECTED) {
                    throw new InvalidRequestException("PAID 상태에서는 ACCEPTED 또는 REJECTED로만 변경할 수 있습니다.");
                }
            }
            case ACCEPTED -> {
                if (next != OrderStatus.SHIPPED) {
                    throw new InvalidRequestException("ACCEPTED 상태에서는 SHIPPED로만 변경할 수 있습니다.");
                }
            }
            case SHIPPED -> {
                if (next != OrderStatus.DELIVERED) {
                    throw new InvalidRequestException("SHIPPED 상태에서는 DELIVERED로만 변경할 수 있습니다.");
                }
            }
            default -> {
                throw new InvalidRequestException("현재 주문 상태에서는 판매자가 상태를 변경할 수 없습니다.");
            }
        }
    }
}