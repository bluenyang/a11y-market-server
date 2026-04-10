package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service;

import com.github.f4b6a3.uuid.alt.GUID;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.S3StorageProperties;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItems;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service.TossPaymentService;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ImageMetadata;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductInquireResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductAiSummaryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductImagesRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service.ProductAnalysisService;
import io.awspring.cloud.s3.S3Template;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    private final OrderItemsRepository orderItemsRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ProductAnalysisService productAnalysisService;
    private final ProductAiSummaryRepository productAiSummaryRepository;
    private final CategoryRepository categoryRepository;
    private final TossPaymentService tossPaymentService;

    @Transactional
    public SellerApplyResponse applySeller(String userEmail, SellerApplyRequest request) {
        Users user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            throw new DataNotFoundException("사용자 정보를 찾을 수 없습니다.");
        }

        if (sellerRepository.existsByUser_UserId(user.getUserId())) {
            throw new DataDuplicatedException("이미 판매자이거나 판매자 신청 이력이 존재합니다.");
        }

        Seller seller = Seller.builder()
                .user(user)
                .sellerName(request.sellerName())
                .businessNumber(request.businessNumber())
                .sellerIntro(request.sellerIntro())
                .build();

        Seller saved = sellerRepository.save(seller);

        return SellerApplyResponse.fromEntity(saved);
    }

    @Transactional
    public ProductDetailResponse registerProduct(String userEmail,
                                                 SellerProductRegisterRequest request,
                                                 List<MultipartFile> images) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보가 존재하지 않습니다. 먼저 판매자 가입 신청을 완료하세요."));

        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품 등록이 가능합니다.");
        }

        Categories category = categoryRepository.getReferenceById(UUID.fromString(request.categoryId()));

        Product product = Product.builder()
                .seller(seller)
                .category(category)
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
                seller.getSellerId(),
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
    public List<ProductInquireResponse> getMyProducts(String userEmail, SellerInquireProductRequest req) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        UUID sellerId = seller.getSellerId();

        var pageable = PageRequest.of(req.page(), req.size());

        var products = productRepository.findBySellerSellerId(sellerId, pageable);

        return products.map(ProductInquireResponse::fromEntity).toList();
    }

    @Transactional
    public ProductDTO updateProduct(String userEmail,
                                    UUID productId,
                                    SellerProductUpdateRequest request,
                                    List<MultipartFile> images) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품을 수정할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSeller().getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품만 수정할 수 있습니다.");
        }

        Categories category = categoryRepository.getReferenceById(UUID.fromString(request.categoryId()));

        product.updateBySeller(
                category,
                request.productName(),
                request.productDescription(),
                request.productPrice(),
                request.productStock(),
                request.productStatus()
        );

        var dbImages = product.getProductImages();
        var requestImages = request.imageMetadataList();

        var requestImageIds = requestImages.stream()
                .map(ImageMetadata::imageId)
                .filter(Objects::nonNull)
                .toList();

        var imagesToDelete = dbImages.stream()
                .filter(img -> !requestImageIds.contains(img.getImageId()))
                .toList();

        for (var img : imagesToDelete) {
            this.deleteImageWithS3(img);
            product.getProductImages().remove(img);
        }

        for (var img : requestImages) {
            if (img.isNew) {
                var newImages = saveImageWithMetadata(
                        images,
                        List.of(img),
                        seller.getSellerId(),
                        product.getProductId()
                );
                product.getProductImages().addAll(newImages);
            } else {
                var existsImage = dbImages.stream()
                        .filter(imgEntity -> imgEntity.getImageId().equals(img.imageId))
                        .findFirst()
                        .orElseThrow(() -> new DataNotFoundException("기존 이미지 정보를 찾을 수 없습니다."));
                existsImage.updateMetadata(img);
            }
        }

        return ProductDTO.fromEntity(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(String userEmail, UUID productId) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품을 삭제할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSeller().getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품만 삭제할 수 있습니다.");
        }

        product.deleteProduct();

        productRepository.save(product);
    }

    @Transactional
    public void deleteProducts(String userEmail, List<Product> products) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품을 삭제할 수 있습니다.");
        }

        for (Product product : products) {
            if (!product.getSeller().getSellerId().equals(seller.getSellerId())) {
                throw new InvalidRequestException("본인의 상품만 삭제할 수 있습니다.");
            }
            product.deleteProduct();
        }

        productRepository.saveAll(products);
    }

    @Transactional
    public ProductDTO updateProductStock(String userEmail, UUID productId, SellerProductStockUpdateRequest request) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("판매자 승인 완료 후 재고를 수정할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSeller().getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품 재고만 수정할 수 있습니다.");
        }

        product.updateStockBySeller(request.productStock());

        return ProductDTO.fromEntity(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public SellerOrderInquireResponse getReceivedOrders(String userEmail,
                                                        OrderItemStatus orderItemStatus,
                                                        Integer page,
                                                        Integer size) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("승인된 판매자만 주문 목록을 조회할 수 있습니다.");
        }

        var pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20
        );
        Page<OrderItems> itemsList;
        int itemCount;

        if (orderItemStatus != null) {
            itemsList = orderItemsRepository
                    .findAllByProduct_Seller_User_UserEmail_AndOrderItemStatus_OrderByOrder_CreatedAtDesc(
                            userEmail,
                            orderItemStatus,
                            pageable);
            itemCount = orderItemsRepository.countAllByProduct_Seller_User_UserEmail_AndOrderItemStatus(
                    userEmail,
                    orderItemStatus);
        } else {
            itemsList = orderItemsRepository
                    .findAllByProduct_Seller_User_UserEmail_OrderByOrder_CreatedAtDesc(userEmail, pageable);
            itemCount = orderItemsRepository
                    .countAllByProduct_Seller_User_UserEmail(
                            userEmail
                    );
        }

        if (itemsList.isEmpty()) {
            return new SellerOrderInquireResponse(List.of(), 0);
        }

        return new SellerOrderInquireResponse(
                itemsList.stream()
                        .map(SellerOrderItemResponse::fromEntity)
                        .toList(),
                itemCount
        );
    }

    @Transactional
    public void updateOrderItemStatus(String userEmail,
                                      UUID orderItemId,
                                      SellerOrderItemsStatusUpdateRequest req) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));
        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("승인된 판매자만 주문 상태를 변경할 수 있습니다.");
        }

        boolean isMyOrder = orderItemsRepository
                .existsByOrderItemIdAndProduct_Seller(orderItemId, seller);
        if (!isMyOrder) {
            throw new InvalidRequestException("해당 주문 상품에 대한 변경 권한이 없습니다.");
        }

        var item = orderItemsRepository.findById(orderItemId)
                .orElseThrow(() -> new DataNotFoundException("주문 상품 정보를 찾을 수 없습니다."));

        var currentStatus = item.getOrderItemStatus();
        var nextStatus = req.status();

        validateSellerOrderItemStatus(currentStatus, nextStatus);

        if (nextStatus == OrderItemStatus.REJECTED && currentStatus == OrderItemStatus.PAID) {
            tossPaymentService.cancelPayment(
                    item.getOrder().getPaymentKey(),
                    "주문 거절에 따른 결제 취소",
                    item.getProductPrice() * item.getProductQuantity()
            );
            item.updateOrderItemStatus(OrderItemStatus.REJECTED);
        } else if (nextStatus == OrderItemStatus.CANCELED) {
            if (currentStatus != OrderItemStatus.ORDERED) {
                try {
                    tossPaymentService.cancelPayment(
                            item.getOrder().getPaymentKey(),
                            "주문 취소 승인",
                            item.getProductPrice() * item.getProductQuantity()
                    );
                } catch (Exception e) {
                    log.error("Error during payment cancellation for orderItemId {}: {}", orderItemId, e.getMessage());
                    throw new InvalidRequestException("결제 취소 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.");
                }
            }
            item.updateOrderItemStatus(OrderItemStatus.CANCELED);
        } else if (nextStatus == OrderItemStatus.RETURNED) {
            tossPaymentService.cancelPayment(
                    item.getOrder().getPaymentKey(),
                    "반품 승인",
                    item.getProductPrice() * item.getProductQuantity()
            );

            item.updateOrderItemStatus(OrderItemStatus.RETURNED);
        } else {
            item.updateOrderItemStatus(req.status());
        }
        orderItemsRepository.save(item);
    }

    private void validateSellerOrderItemStatus(OrderItemStatus current, OrderItemStatus next) {

        if (current == next) {
            throw new InvalidRequestException("이미 동일한 주문 상태입니다.");
        }

        switch (current) {
            case ORDERED -> {
                if (next != OrderItemStatus.PAID && next != OrderItemStatus.CANCELED) {
                    throw new InvalidRequestException("'결제대기' 상태에서는 '결제됨' 또는 '취소됨'으로만 변경할 수 있습니다.");
                }
            }

            case PAID -> {
                if (next != OrderItemStatus.ACCEPTED && next != OrderItemStatus.REJECTED) {
                    throw new InvalidRequestException("'결제됨' 상태에서는 '주문 접수됨' 또는 '주문 거절됨'으로만 변경할 수 있습니다.");
                }
            }
            case ACCEPTED -> {
                if (next != OrderItemStatus.SHIPPING) {
                    throw new InvalidRequestException("'주문 접수됨' 상태에서는 '배송 중'으로만 변경할 수 있습니다.");
                }
            }
            case SHIPPING -> {
                if (next != OrderItemStatus.SHIPPED) {
                    throw new InvalidRequestException("'배송 중' 상태에서는 '배송 완료'로만 변경할 수 있습니다.");
                }
            }
            case CANCEL_PENDING -> {
                if (next != OrderItemStatus.CANCELED && next != OrderItemStatus.CANCEL_REJECTED) {
                    throw new InvalidRequestException("'취소 요청' 상태에서는 '취소됨' 또는 '취소 거절'만 가능합니다.");
                }
            }
            case RETURN_PENDING -> {
                if (next != OrderItemStatus.RETURNED && next != OrderItemStatus.RETURN_REJECTED) {
                    throw new InvalidRequestException("'반품 요청' 상태에서는 '반품 완료' 또는 '반품 거절'만 가능합니다.");
                }
            }
            default -> throw new InvalidRequestException("현재 주문 상태에서는 판매자가 상태를 변경할 수 없습니다. 관리자에게 문의하세요.");

        }
    }

    @Transactional
    public void processOrderClaim(String userEmail, UUID orderItemId, SellerOrderClaimProcessRequest request) {
        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("승인된 판매자만 취소/반품 처리가 가능합니다.");
        }

        OrderItems orderItem = orderItemsRepository.findById(orderItemId)
                .orElseThrow(() -> new DataNotFoundException("주문 상품 정보를 찾을 수 없습니다."));

        Product product = orderItem.getProduct();
        if (product == null) {
            throw new DataNotFoundException("주문한 상품 정보를 찾을 수 없습니다.");
        }

        var productSeller = product.getSeller();
        if (productSeller == null) {
            throw new DataNotFoundException("주문 상품의 판매자 정보를 찾을 수 없습니다.");
        }


        if (!productSeller.getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품 주문만 처리할 수 있습니다.");
        }

        OrderItemStatus currentStatus = orderItem.getOrderItemStatus();

        if (currentStatus != OrderItemStatus.CANCEL_PENDING &&
                currentStatus != OrderItemStatus.RETURN_PENDING) {
            throw new InvalidRequestException("요청 상태의 주문만 처리할 수 있습니다.");
        }

        if (request.action().isApproved()) {
            if (currentStatus == OrderItemStatus.CANCEL_PENDING) {
                tossPaymentService.cancelPayment(
                        orderItem.getOrder().getPaymentKey(),
                        "주문 취소 승인",
                        orderItem.getProductPrice() * orderItem.getProductQuantity()
                );

                orderItem.updateOrderItemStatus(OrderItemStatus.CANCELED);
            } else {
                // 위에서 이미 걸러 냈기 때문에 else if 문은 필요하지 않음
                tossPaymentService.cancelPayment(
                        orderItem.getOrder().getPaymentKey(),
                        "반품 승인",
                        orderItem.getProductPrice() * orderItem.getProductQuantity()
                );

                orderItem.updateOrderItemStatus(OrderItemStatus.RETURNED);
            }
        } else {
            orderItem.updateOrderItemStatus(OrderItemStatus.SHIPPED);
        }

        orderItemsRepository.save(orderItem);
    }

    @Transactional(readOnly = true)
    public List<SellerOrderItemResponse> getOrderClaims(String userEmail) {

        Seller seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().isApproved()) {
            throw new InvalidRequestException("승인된 판매자만 취소/반품/교환 목록을 조회할 수 있습니다.");
        }

        List<OrderItemStatus> claimStatuses = List.of(
                OrderItemStatus.CANCEL_PENDING,
                OrderItemStatus.RETURN_PENDING
        );

        var claimItems = orderItemsRepository
                .findAllByProduct_Seller_User_UserEmail_AndOrderItemStatusIn(
                        userEmail,
                        claimStatuses);

        return claimItems.stream().map(SellerOrderItemResponse::fromEntity).toList();
    }

    public SellerInfoResponse getSellerInfoById(UUID sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        return SellerInfoResponse.fromEntity(seller);
    }

    private List<ProductImages> saveImageWithMetadata(List<MultipartFile> images,
                                                      List<ImageMetadata> metadataList,
                                                      UUID sellerId,
                                                      UUID productId) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        Map<String, MultipartFile> fileMap = images.stream()
                .collect(Collectors.toMap(MultipartFile::getOriginalFilename, Function.identity()));

        List<ProductImages> savedImages = new ArrayList<>();

        for (var meta : metadataList) {
            var image = fileMap.get(meta.originalFileName);

            String imageUrl = uploadImageToS3(image, sellerId, productId);
            var savedImg = productImagesRepository.save(
                    ProductImages.builder()
                            .product(productRepository.getReferenceById(productId))
                            .imageUrl(imageUrl)
                            .altText(meta.altText)
                            .imageSequence(meta.sequence)
                            .build()
            );

            savedImages.add(savedImg);
        }

        return savedImages;
    }

    private String uploadImageToS3(MultipartFile image, UUID sellerId, UUID productId) {
        log.debug("Uploading image to S3 for sellerId: {}, productId: {}", sellerId, productId);

        if (image.isEmpty()) {
            log.info("Image is empty for sellerId: {}, productId: {}", sellerId, productId);
            return null;
        }
        // 파일 위치 => /images/{sellerId}/{productId}/{생성된 UUID}.format 으로 저장
        String folder = "images/" + sellerId + "/" + productId;

        // unique한 파일명 생성
        String originalFilename = image.getOriginalFilename();
        UUID fileId = GUID.v7().toUUID();
        String uniqueFileName = folder + "/" + fileId + "_" + originalFilename;

        try {
            String bucketName = this.s3StorageProperties.getBucket();
            this.s3Template.upload(bucketName,
                    uniqueFileName,
                    image.getInputStream());

            return "/" + bucketName + "/" + uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteImageWithS3(ProductImages img) {
        if (img == null || img.getImageUrl() == null) {
            log.debug("Image or image URL is null, skipping S3 deletion.");
            return;
        }

        log.debug("Deleting image from S3: {}", img.getImageUrl());

        var bucketAndKey = img.getImageUrl().substring(1); // 앞의 '/' 제거
        var splitIndex = bucketAndKey.indexOf('/');
        if (splitIndex == -1) {
            log.warn("Invalid S3 image URL format: {}", img.getImageUrl());
            return;
        }

        try {
            String bucketName = bucketAndKey.substring(0, splitIndex);
            String objectKey = bucketAndKey.substring(splitIndex + 1);

            this.s3Template.deleteObject(bucketName, objectKey);
            log.debug("Deleted image from S3: bucket={}, key={}", bucketName, objectKey);
            this.productImagesRepository.delete(img);
        } catch (Exception e) {
            log.error("Failed to delete image from S3: {}", img.getImageUrl(), e);
        }
    }

    private ProductAiSummary createAiSummaryForProduct(UUID productId,
                                                       String productName,
                                                       String productDescription,
                                                       List<MultipartFile> images) {

        var result = productAnalysisService.analysisProductImage(productName, productDescription, images);
        return ProductAiSummary.builder()
                .product(productRepository.getReferenceById(productId))
                .summaryText(result.summary())
                .usageContext(result.usageContext())
                .usageMethod(result.usageMethod())
                .build();
    }

    public SellerInfoResponse updateSellerInfo(String userEmail, @Valid SellerUpdateRequest req) {
        var seller = sellerRepository.findByUser_UserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보가 존재하지 않습니다."));

        seller.updateSellerInfo(
                req.sellerName(),
                req.sellerIntro()
        );

        return SellerInfoResponse.fromEntity(sellerRepository.save(seller));
    }

    public SellerOrderSummaryResponse getOrderSummary(String userEmail) {
        List<Object[]> result = orderItemsRepository.countOrderItemsByStatusGroupedBySellerUserEmail(userEmail);

        long newOrders = 0;
        long acceptedOrders = 0;
        long shippingOrders = 0;
        long completedOrders = 0;
        long claimedOrders = 0;

        for (Object[] row : result) {
            OrderItemStatus status = (OrderItemStatus) row[0];
            Long count = (Long) row[1];

            switch (status) {
                case ORDERED -> newOrders = count;
                case ACCEPTED -> acceptedOrders = count;
                case SHIPPING -> shippingOrders = count;
                case SHIPPED -> completedOrders = count;
                case CANCEL_PENDING, RETURN_PENDING -> claimedOrders += count;
                default -> {
                    // 기타 상태는 무시
                }
            }
        }

        return new SellerOrderSummaryResponse(
                newOrders,
                acceptedOrders,
                shippingOrders,
                completedOrders,
                claimedOrders
        );
    }
}