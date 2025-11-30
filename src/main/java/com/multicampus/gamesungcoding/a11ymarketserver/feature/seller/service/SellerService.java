package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service;

import com.github.f4b6a3.uuid.alt.GUID;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.S3StorageProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.Orders;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrdersRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ImageMetadata;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductAiSummary;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductAiSummaryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductImagesRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerGrades;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSales;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.SellerSubmitStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerSalesRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service.ProductAnalysisService;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SellerService {

    private final S3Template s3Template;
    private final S3StorageProperties s3StorageProperties;

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final SellerSalesRepository sellerSalesRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ProductAnalysisService productAnalysisService;
    private final ProductAiSummaryRepository productAiSummaryRepository;

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

    public ProductDetailResponse registerProduct(String userEmail,
                                                 SellerProductRegisterRequest request,
                                                 List<MultipartFile> images) {

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


        product = productRepository.save(product);

        if (images == null || images.isEmpty() || request.imageMetadataList() == null) {
            return ProductDetailResponse.fromEntity(product, null, null);
        }

        List<ProductImages> savedImages = saveImageWithMetadata(
                images,
                request.imageMetadataList(),
                sellerId,
                product.getProductId()
        );

        return ProductDetailResponse.fromEntity(
                productRepository.save(product),
                savedImages,
                productAiSummaryRepository.save(
                        createAiSummaryForProduct(
                                product.getProductId(),
                                product.getProductName(),
                                product.getProductDescription(),
                                images
                        )
                )
        );
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
            default -> throw new InvalidRequestException("현재 주문 상태에서는 판매자가 상태를 변경할 수 없습니다.");

        }
    }

    @Transactional
    public void processOrderClaim(String userEmail, UUID orderItemId, SellerOrderClaimProcessRequest request) {
        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!SellerSubmitStatus.APPROVED.getStatus().equals(seller.getSellerSubmitStatus())) {
            throw new InvalidRequestException("승인된 판매자만 취소/반품 처리가 가능합니다.");
        }

        OrderItems orderItem = orderItemsRepository.findById(orderItemId)
                .orElseThrow(() -> new DataNotFoundException("주문 상품 정보를 찾을 수 없습니다."));

        Product product = productRepository.findById(orderItem.getProductId())
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품 주문만 처리할 수 있습니다.");
        }

        OrderItemStatus currentStatus = orderItem.getOrderItemStatus();

        if (currentStatus != OrderItemStatus.CANCEL_PENDING &&
                currentStatus != OrderItemStatus.RETURN_PENDING) {
            throw new InvalidRequestException("요청 상태의 주문만 처리할 수 있습니다.");
        }

        String action = request.action().toUpperCase();

        if ("APPROVE".equals(action)) {
            if (currentStatus == OrderItemStatus.CANCEL_PENDING) {
                orderItem.updateOrderItemStatus(OrderItemStatus.CANCELED);
            } else {
                // 위에서 이미 걸러 냈기 때문에 else if 문은 필요하지 않음
                orderItem.updateOrderItemStatus(OrderItemStatus.RETURNED);
            }
        } else if ("REJECT".equals(action)) {

            orderItem.updateOrderItemStatus(OrderItemStatus.ORDERED);
        } else {
            throw new InvalidRequestException("유효하지 않은 처리 요청입니다. (APPROVE / REJECT)");
        }

        orderItemsRepository.save(orderItem);
    }

    @Transactional(readOnly = true)
    public List<SellerOrderItemResponse> getOrderClaims(String userEmail) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!SellerSubmitStatus.APPROVED.getStatus().equals(seller.getSellerSubmitStatus())) {
            throw new InvalidRequestException("승인된 판매자만 취소/반품/교환 목록을 조회할 수 있습니다.");
        }

        List<OrderItemStatus> claimStatuses = List.of(
                OrderItemStatus.CANCEL_PENDING,
                OrderItemStatus.RETURN_PENDING
        );

        return orderItemsRepository.findSellerClaims(userEmail, claimStatuses);
    }

    @Transactional(readOnly = true)
    public SellerDashboardResponse getDashboard(String userEmail) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!SellerSubmitStatus.APPROVED.getStatus().equals(seller.getSellerSubmitStatus())) {
            throw new InvalidRequestException("승인된 판매자만 대시보드를 조회할 수 있습니다.");
        }

        SellerSales sales = sellerSalesRepository.findBySellerId(seller.getSellerId())
                .orElse(null);

        int totalSales = sales != null && sales.getTotalSales() != null ? sales.getTotalSales() : 0;
        int totalOrders = sales != null && sales.getTotalOrders() != null ? sales.getTotalOrders() : 0;
        int totalProductsSold = sales != null && sales.getTotalProductsSold() != null ? sales.getTotalProductsSold() : 0;
        int totalCancelled = sales != null && sales.getTotalCancelled() != null ? sales.getTotalCancelled() : 0;

        return new SellerDashboardResponse(
                seller.getSellerId(),
                seller.getSellerName(),
                totalSales,
                totalOrders,
                totalProductsSold,
                totalCancelled
        );
    }

    private List<ProductImages> saveImageWithMetadata(List<MultipartFile> images,
                                                      List<ImageMetadata> metadataList,
                                                      UUID sellerId,
                                                      UUID productId) {
        Map<String, MultipartFile> fileMap = images.stream()
                .collect(Collectors.toMap(MultipartFile::getOriginalFilename, Function.identity()));

        List<ProductImages> savedImages = new ArrayList<>();

        for (var meta : metadataList) {
            var image = fileMap.get(meta.originalFileName());

            String imageUrl = uploadImageToS3(image, sellerId, productId);
            var savedImg = productImagesRepository.save(
                    ProductImages.builder()
                            .productId(productId)
                            .imageUrl(imageUrl)
                            .altText(meta.altText())
                            .imageSequence(meta.sequence())
                            .build()
            );

            savedImages.add(savedImg);
        }

        return savedImages;
    }

    private String uploadImageToS3(MultipartFile image, UUID sellerId, UUID productId) {
        if (image.isEmpty()) {
            return null;
        }
        // 파일 위치 => /images/{sellerId}/{productId}/{생성된 UUID}.format 으로 저장
        String folder = "images/" + sellerId + "/" + productId;

        // unique한 파일명 생성
        String originalFilename = image.getOriginalFilename();
        UUID fileId = GUID.v7().toUUID();
        String uniqueFileName = folder + "/" + fileId + "_" + originalFilename;

        try {
            String bucketName = s3StorageProperties.getBucket();
            s3Template.upload(bucketName,
                    uniqueFileName,
                    image.getInputStream());

            return "/" + bucketName + "/" + uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ProductAiSummary createAiSummaryForProduct(UUID productId,
                                                       String productName,
                                                       String productDescription,
                                                       List<MultipartFile> images) {

        var result = productAnalysisService.analysisProductImage(productName, productDescription, images);
        return ProductAiSummary.builder()
                .productId(productId)
                .summaryText(result.summary())
                .usageContext(result.usageContext())
                .usageMethod(result.usageMethod())
                .build();
    }
}