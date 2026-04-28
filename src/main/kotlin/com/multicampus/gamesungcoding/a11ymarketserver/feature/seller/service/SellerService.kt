package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service

import com.github.f4b6a3.uuid.alt.GUID
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.common.properties.S3StorageProperties
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.entity.OrderItemStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.repository.OrderItemsRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.order.service.TossPaymentService
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ImageMetadata
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductInquireResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductAiSummary
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper.toDTO
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper.toDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper.toInquireResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductAiSummaryRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductImagesRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper.toApplyResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper.toInfoResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.mapper.toSellerOrderItemResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository
import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service.ProductAnalysisService
import io.awspring.cloud.s3.S3Template
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*
import java.util.function.Supplier

@Service
@Transactional
class SellerService(
    private val s3Template: S3Template,
    private val s3StorageProperties: S3StorageProperties,

    private val sellerRepository: SellerRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val orderItemsRepository: OrderItemsRepository,
    private val productImagesRepository: ProductImagesRepository,
    private val productAnalysisService: ProductAnalysisService,
    private val productAiSummaryRepository: ProductAiSummaryRepository,
    private val categoryRepository: CategoryRepository,
    private val tossPaymentService: TossPaymentService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun applySeller(userEmail: String, request: SellerApplyRequest): SellerApplyResponse {
        val user = userRepository.findByUserEmail(userEmail)
            ?: throw UserNotFoundException("사용자 정보를 찾을 수 없습니다.")

        val userId = requireNotNull(user.userId) { "사용자 식별자가 존재하지 않습니다." }

        if (sellerRepository.existsByUserUserId(userId)) {
            throw DataDuplicatedException("이미 판매자이거나 판매자 신청 이력이 존재합니다.")
        }

        val saved = sellerRepository.save(
            Seller(
                user = user,
                sellerName = request.sellerName,
                businessNumber = request.businessNumber,
                sellerIntro = request.sellerIntro
            )
        )

        return saved.toApplyResponse()
    }

    fun registerProduct(
        userEmail: String,
        request: SellerProductRegisterRequest,
        images: List<MultipartFile>
    ): ProductDetailResponse {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보가 존재하지 않습니다. 먼저 판매자 가입 신청을 완료하세요.")


        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("판매자 승인 완료 후 상품 등록이 가능합니다.")
        }

        val category = categoryRepository.getReferenceById(UUID.fromString(request.categoryId))

        var product = Product(
            seller,
            category,
            request.productPrice,
            request.productStock,
            request.productName,
            request.productDescription,
            ProductStatus.PENDING
        )


        product = productRepository.save(product)

        if (images.isEmpty() || request.imageMetadataList.isEmpty()) {
            product.toDetailResponse(null, null);
        }

        val savedImages = saveImageWithMetadata(
            images,
            request.imageMetadataList,
            seller.sellerId,
            product.productId!!
        )

        return productRepository.save(product).toDetailResponse(
            savedImages,
            productAiSummaryRepository.save(
                createAiSummaryForProduct(
                    product.productId!!,
                    product.productName,
                    product.productDescription,
                    images
                )
            )
        )
    }

    @Transactional(readOnly = true)
    fun getMyProducts(userEmail: String, req: SellerInquireProductRequest): List<ProductInquireResponse> {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        requireNotNull(seller.sellerId) {
            throw DataNotFoundException("조회된 객체의 Id는 null일 수 없습니다.")
        }

        val pageable = PageRequest.of(req.page, req.size)
        val products = productRepository.findBySellerSellerId(seller.sellerId, pageable)

        return products.map { it.toInquireResponse() }.toList()
    }

    fun updateProduct(
        userEmail: String,
        productId: UUID,
        request: SellerProductUpdateRequest,
        images: List<MultipartFile>?
    ): ProductDTO {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("판매자 승인 완료 후 상품을 수정할 수 있습니다.")
        }

        val product = productRepository.findByIdOrNull(productId)
            ?: throw DataNotFoundException("상품 정보를 찾을 수 없습니다.")

        if (product.seller?.sellerId != seller.sellerId) {
            throw InvalidRequestException("본인의 상품만 수정할 수 있습니다.")
        }

        val category = categoryRepository.getReferenceById(UUID.fromString(request.categoryId))

        product.updateBySeller(
            category,
            request.productName,
            request.productDescription,
            request.productPrice,
            request.productStock,
            request.productStatus
        )

        val dbImages = product.productImages
        val requestImages = request.imageMetadataList ?: emptyList()

        val requestImageIds = requestImages.map { it.imageId }
        val imagesToDelete = dbImages.filter { it.imageId !in requestImageIds }

        imagesToDelete.forEach {
            this.deleteImageWithS3(it)
            product.productImages.remove(it)
        }

        requestImages.forEach { imgMeta ->
            if (imgMeta.isNew) {
                if (!images.isNullOrEmpty()) {
                    val newImages = saveImageWithMetadata(
                        images,
                        listOf(imgMeta),
                        seller.sellerId,
                        product.productId ?: throw DataNotFoundException("ProductId not found")
                    )
                    product.productImages.addAll(newImages)
                }
            } else {
                val existsImage = dbImages.find { it.imageId == imgMeta.imageId }
                    ?: throw DataNotFoundException("기존 이미지 정보를 찾을 수 없습니다.")
                existsImage.updateMetadata(imgMeta)
            }
        }

        return productRepository.save(product).toDTO()
    }

    fun deleteProduct(userEmail: String, productId: UUID) {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("판매자 승인 완료 후 상품을 삭제할 수 있습니다.")
        }

        val product = productRepository.findById(productId)
            .orElseThrow(Supplier { DataNotFoundException("상품 정보를 찾을 수 없습니다.") })

        if (product.seller?.sellerId != seller.sellerId) {
            throw InvalidRequestException("본인의 상품만 삭제할 수 있습니다.")
        }

        product.deleteProduct()
        productRepository.save(product)
    }

    fun deleteProducts(userEmail: String, products: List<Product>) {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("판매자 승인 완료 후 상품을 삭제할 수 있습니다.")
        }

        products.forEach { product ->
            if (product.seller?.sellerId != seller.sellerId) {
                throw InvalidRequestException("본인의 상품만 삭제할 수 있습니다.")
            }
            product.deleteProduct()
        }

        productRepository.saveAll(products)
    }

    fun updateProductStock(userEmail: String, productId: UUID, request: SellerProductStockUpdateRequest): ProductDTO {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("판매자 승인 완료 후 재고를 수정할 수 있습니다.")
        }

        val product = productRepository.findByIdOrNull(productId)
            ?: throw DataNotFoundException("상품 정보를 찾을 수 없습니다.")

        if (product.seller?.sellerId != seller.sellerId) {
            throw InvalidRequestException("본인의 상품 재고만 수정할 수 있습니다.")
        }

        product.updateStockBySeller(request.productStock)

        return productRepository.save(product).toDTO()
    }

    @Transactional(readOnly = true)
    fun getReceivedOrders(
        userEmail: String,
        orderItemStatus: OrderItemStatus?,
        page: Int?,
        size: Int?
    ): SellerOrderInquireResponse {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("승인된 판매자만 주문 목록을 조회할 수 있습니다.")
        }

        val pageable = PageRequest.of(page ?: 0, size ?: 20)

        val (itemsList, itemCount) = if (orderItemStatus != null) {
            orderItemsRepository.findAllByProduct_Seller_User_UserEmail_AndOrderItemStatus_OrderByOrder_CreatedAtDesc(
                userEmail,
                orderItemStatus,
                pageable
            ) to orderItemsRepository.countAllByProduct_Seller_User_UserEmail_AndOrderItemStatus(
                userEmail,
                orderItemStatus
            )
        } else {
            orderItemsRepository.findAllByProduct_Seller_User_UserEmail_OrderByOrder_CreatedAtDesc(
                userEmail,
                pageable
            ) to orderItemsRepository.countAllByProduct_Seller_User_UserEmail(userEmail)
        }

        if (itemsList.isEmpty) {
            return SellerOrderInquireResponse(emptyList(), 0)
        }

        return SellerOrderInquireResponse(
            itemsList.map { it.toSellerOrderItemResponse() }.toList(),
            itemCount
        )
    }

    @Transactional
    fun updateOrderItemStatus(
        userEmail: String,
        orderItemId: UUID,
        req: SellerOrderItemsStatusUpdateRequest
    ) {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("승인된 판매자만 주문 상태를 변경할 수 있습니다.")
        }

        if (!orderItemsRepository.existsByOrderItemIdAndProduct_Seller(orderItemId, seller)) {
            throw InvalidRequestException("해당 주문 상품에 대한 변경 권한이 없습니다.")
        }

        val item = orderItemsRepository.findByIdOrNull(orderItemId)
            ?: throw DataNotFoundException("주문 상품 정보를 찾을 수 없습니다.")

        val currentStatus = item.orderItemStatus
        val nextStatus = req.status

        validateSellerOrderItemStatus(currentStatus, nextStatus)

        when (nextStatus) {
            OrderItemStatus.REJECTED -> {
                if (currentStatus === OrderItemStatus.PAID) {
                    tossPaymentService.cancelPayment(
                        item.order.paymentKey,
                        "주문 거절에 따른 결제 취소",
                        item.productPrice * item.productQuantity
                    )
                    item.updateOrderItemStatus(OrderItemStatus.REJECTED)
                }
            }

            OrderItemStatus.CANCELED -> {
                if (currentStatus != OrderItemStatus.ORDERED) {
                    try {
                        tossPaymentService.cancelPayment(
                            item.order.paymentKey,
                            "주문 취소 승인",
                            item.productPrice * item.productQuantity
                        )
                    } catch (e: Exception) {
                        log.error(
                            "Error during payment cancellation for orderItemId {}: {}",
                            orderItemId,
                            e.message
                        )
                        throw InvalidRequestException("결제 취소 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.")
                    }
                }
                item.updateOrderItemStatus(OrderItemStatus.CANCELED)
            }

            OrderItemStatus.RETURNED -> {
                tossPaymentService.cancelPayment(
                    item.order.paymentKey,
                    "반품 승인",
                    item.productPrice * item.productQuantity
                )

                item.updateOrderItemStatus(OrderItemStatus.RETURNED)
            }

            else -> {
                item.updateOrderItemStatus(req.status)
            }
        }

        orderItemsRepository.save(item)
    }

    private fun validateSellerOrderItemStatus(current: OrderItemStatus, next: OrderItemStatus?) {
        if (current == next) {
            throw InvalidRequestException("이미 동일한 주문 상태입니다.")
        }

        when (current) {
            OrderItemStatus.ORDERED -> {
                if (next != OrderItemStatus.PAID && next != OrderItemStatus.CANCELED) {
                    throw InvalidRequestException("'결제대기' 상태에서는 '결제됨' 또는 '취소됨'으로만 변경할 수 있습니다.")
                }
            }

            OrderItemStatus.PAID -> {
                if (next != OrderItemStatus.ACCEPTED && next != OrderItemStatus.REJECTED) {
                    throw InvalidRequestException("'결제됨' 상태에서는 '주문 접수됨' 또는 '주문 거절됨'으로만 변경할 수 있습니다.")
                }
            }

            OrderItemStatus.ACCEPTED -> {
                if (next != OrderItemStatus.SHIPPING) {
                    throw InvalidRequestException("'주문 접수됨' 상태에서는 '배송 중'으로만 변경할 수 있습니다.")
                }
            }

            OrderItemStatus.SHIPPING -> {
                if (next != OrderItemStatus.SHIPPED) {
                    throw InvalidRequestException("'배송 중' 상태에서는 '배송 완료'로만 변경할 수 있습니다.")
                }
            }

            OrderItemStatus.CANCEL_PENDING -> {
                if (next != OrderItemStatus.CANCELED && next != OrderItemStatus.CANCEL_REJECTED) {
                    throw InvalidRequestException("'취소 요청' 상태에서는 '취소됨' 또는 '취소 거절'만 가능합니다.")
                }
            }

            OrderItemStatus.RETURN_PENDING -> {
                if (next != OrderItemStatus.RETURNED && next != OrderItemStatus.RETURN_REJECTED) {
                    throw InvalidRequestException("'반품 요청' 상태에서는 '반품 완료' 또는 '반품 거절'만 가능합니다.")
                }
            }

            else -> throw InvalidRequestException("현재 주문 상태에서는 판매자가 상태를 변경할 수 없습니다. 관리자에게 문의하세요.")
        }
    }

    fun processOrderClaim(userEmail: String, orderItemId: UUID, request: SellerOrderClaimProcessRequest) {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("승인된 판매자만 취소/반품 처리가 가능합니다.")
        }

        val orderItem = orderItemsRepository.findByIdOrNull(orderItemId)
            ?: throw DataNotFoundException("주문 상품 정보를 찾을 수 없습니다.")

        val product = orderItem.product
            ?: throw DataNotFoundException("주문한 상품 정보를 찾을 수 없습니다.")

        val productSeller = product.seller
            ?: throw DataNotFoundException("주문 상품의 판매자 정보를 찾을 수 없습니다.")


        if (productSeller.sellerId != seller.sellerId) {
            throw InvalidRequestException("본인의 상품 주문만 처리할 수 있습니다.")
        }

        val currentStatus = orderItem.orderItemStatus

        if (currentStatus != OrderItemStatus.CANCEL_PENDING && currentStatus != OrderItemStatus.RETURN_PENDING) {
            throw InvalidRequestException("요청 상태의 주문만 처리할 수 있습니다.")
        }

        if (request.action.isApproved()) {
            if (currentStatus == OrderItemStatus.CANCEL_PENDING) {
                tossPaymentService.cancelPayment(
                    orderItem.order.paymentKey,
                    "주문 취소 승인",
                    orderItem.productPrice * orderItem.productQuantity
                )

                orderItem.updateOrderItemStatus(OrderItemStatus.CANCELED)
            } else {
                // 위에서 이미 걸러 냈기 때문에 else if 문은 필요하지 않음
                tossPaymentService.cancelPayment(
                    orderItem.order.paymentKey,
                    "반품 승인",
                    orderItem.productPrice * orderItem.productQuantity
                )

                orderItem.updateOrderItemStatus(OrderItemStatus.RETURNED)
            }
        } else {
            orderItem.updateOrderItemStatus(OrderItemStatus.SHIPPED)
        }

        orderItemsRepository.save(orderItem)
    }

    @Transactional(readOnly = true)
    fun getOrderClaims(userEmail: String): List<SellerOrderItemResponse> {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        if (!seller.sellerSubmitStatus.isApproved) {
            throw InvalidRequestException("승인된 판매자만 취소/반품/교환 목록을 조회할 수 있습니다.")
        }

        val claimStatuses = listOf(
            OrderItemStatus.CANCEL_PENDING,
            OrderItemStatus.RETURN_PENDING
        )

        val claimItems = orderItemsRepository
            .findAllByProduct_Seller_User_UserEmail_AndOrderItemStatusIn(
                userEmail,
                claimStatuses
            )

        return claimItems.map { it.toSellerOrderItemResponse() }
    }

    fun getSellerInfoById(sellerId: UUID): SellerInfoResponse {
        val seller = sellerRepository.findByIdOrNull(sellerId)
            ?: throw DataNotFoundException("판매자 정보를 찾을 수 없습니다.")

        return seller.toInfoResponse()
    }

    private fun saveImageWithMetadata(
        images: List<MultipartFile>,
        metadataList: List<ImageMetadata>,
        sellerId: UUID?,
        productId: UUID
    ): List<ProductImages> {
        if (images.isEmpty()) {
            return listOf()
        }

        val fileMap = images.associateBy { it.originalFilename }
        val savedImages: ArrayList<ProductImages> = arrayListOf()

        metadataList.forEach {
            val image: MultipartFile = fileMap[it.originalFileName]
                ?: throw DataNotFoundException("Filename not found")

            val imageUrl = uploadImageToS3(image, sellerId, productId)
            val savedImg: ProductImages = productImagesRepository.save(
                ProductImages(
                    productRepository.getReferenceById(productId),
                    imageUrl,
                    it.altText,
                    it.sequence
                )
            )
            savedImages.add(savedImg)
        }

        return savedImages
    }

    private fun uploadImageToS3(image: MultipartFile, sellerId: UUID?, productId: UUID?): String? {
        log.debug("Uploading image to S3 for sellerId: {}, productId: {}", sellerId, productId)

        if (image.isEmpty) {
            log.info("Image is empty for sellerId: {}, productId: {}", sellerId, productId)
            return null
        }
        // unique한 파일명 생성
        // 파일 위치 => /images/{sellerId}/{productId}/{생성된 UUID}.format 으로 저장
        val uniqueFileName = "images/$sellerId/$productId/${GUID.v7().toUUID()}_${image.originalFilename}"

        try {
            val bucketName = this.s3StorageProperties.bucket
            this.s3Template.upload(
                bucketName,
                uniqueFileName,
                image.inputStream
            )

            return "/$bucketName/$uniqueFileName"
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun deleteImageWithS3(img: ProductImages) {
        log.debug("Deleting image from S3: {}", img.imageUrl)

        val bucketAndKey = img.imageUrl?.substring(1) // 앞의 '/' 제거
            ?: throw DataNotFoundException("Image or image URL is null, skipping S3 deletion.")

        val splitIndex = bucketAndKey.indexOf('/')
        if (splitIndex == -1) {
            log.warn("Invalid S3 image URL format: {}", img.imageUrl)
            return
        }

        try {
            val bucketName = bucketAndKey.substring(0, splitIndex)
            val objectKey = bucketAndKey.substring(splitIndex + 1)

            this.s3Template.deleteObject(bucketName, objectKey)
            log.debug("Deleted image from S3: bucket={}, key={}", bucketName, objectKey)

            this.productImagesRepository.delete(img)
        } catch (e: Exception) {
            log.error("Failed to delete image from S3: {}", img.imageUrl, e)
        }
    }

    private fun createAiSummaryForProduct(
        productId: UUID,
        productName: String?,
        productDescription: String?,
        images: List<MultipartFile>?
    ): ProductAiSummary {
        val result = productAnalysisService.analysisProductImage(
            productName,
            productDescription,
            images
        )

        return ProductAiSummary(
            productRepository.getReferenceById(productId),
            result.summary,
            result.usageContext,
            result.usageMethod
        )
    }

    fun updateSellerInfo(userEmail: String, @Valid req: @Valid SellerUpdateRequest): SellerInfoResponse {
        val seller = sellerRepository.findByUserUserEmail(userEmail)
            ?: throw DataNotFoundException("판매자 정보가 존재하지 않습니다.")

        seller.updateSellerInfo(
            req.sellerName,
            req.sellerIntro
        )

        return sellerRepository.save(seller).toInfoResponse()
    }

    @Transactional(readOnly = true)
    fun getOrderSummary(userEmail: String?): SellerOrderSummaryResponse {
        val result = orderItemsRepository.countOrderItemsByStatusGroupedBySellerUserEmail(userEmail)

        val statusCountMap = result.associate { row ->
            (row[0] as OrderItemStatus) to (row[1] as Long)
        }

        val newOrders = statusCountMap[OrderItemStatus.ORDERED] ?: 0L
        val acceptedOrders = statusCountMap[OrderItemStatus.ACCEPTED] ?: 0L
        val shippingOrders = statusCountMap[OrderItemStatus.SHIPPING] ?: 0L
        val completedOrders = statusCountMap[OrderItemStatus.SHIPPED] ?: 0L
        val claimedOrders = (statusCountMap[OrderItemStatus.CANCEL_PENDING] ?: 0L) +
                (statusCountMap[OrderItemStatus.RETURN_PENDING] ?: 0L)

        return SellerOrderSummaryResponse(
            newOrders,
            acceptedOrders,
            shippingOrders,
            completedOrders,
            claimedOrders
        )
    }
}