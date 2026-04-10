package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductAiSummaryRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductImagesRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller
import jakarta.persistence.criteria.*
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Supplier

/**
 * - search 파라미터 유무에 따라 전체/필터 조회
 * - certified/grade는 스펙 확장 시 반영
 */
@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productImagesRepository: ProductImagesRepository,
    private val productAiSummaryRepository: ProductAiSummaryRepository
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    fun getProducts(
        search: String?,
        certified: Boolean?,
        grade: String?,
        categoryIds: List<String>?
    ): List<ProductResponse> {
        val spec = Specification<Product> { root: Root<Product>, _: CriteriaQuery<*>, builder: CriteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            predicates.add(builder.equal(root.get<String>("productStatus"), ProductStatus.APPROVED.name))

            if (!search.isNullOrBlank()) {
                predicates.add(builder.like(root.get("productName"), "%$search%"))
            }

            if (certified == true || !grade.isNullOrBlank()) {
                val sellerJoin = root.join<Product, Seller>("seller", JoinType.INNER)

                if (certified == true) { // Nullable Boolean
                    predicates.add(builder.equal(sellerJoin.get<Boolean>("isA11yGuarantee"), true))
                }
                if (!grade.isNullOrBlank()) {
                    predicates.add(builder.equal(sellerJoin.get<String>("sellerGrade"), grade))
                }
            }

            if (!categoryIds.isNullOrEmpty()) {
                val categoryJoin = root.join<Product, Categories>("category", JoinType.INNER)
                val uuidList = categoryIds.map { UUID.fromString(it) }

                val isCategory = categoryJoin.get<UUID>("categoryId").`in`(uuidList)
                val isParentCategory = categoryJoin.get<Categories>("parentCategory")
                    .get<UUID>("categoryId")
                    .`in`(uuidList)

                predicates.add(builder.or(isCategory, isParentCategory))
            }
            builder.and(*predicates.toTypedArray())
        }


        val list = productRepository.findAll(spec)
            .map { ProductResponse.fromEntity(it) }

        log.debug("Filtered products count: {}", list.size)
        return list
    }

    @Transactional(readOnly = true)
    fun getProductDetail(productId: UUID): ProductDetailResponse {
        val product = productRepository.findById(productId)
            .orElseThrow(Supplier {
                DataNotFoundException("Invalid product ID: $productId")
            })

        val productImages = productImagesRepository.findAllByProduct(product)
        val productAiSummary = productAiSummaryRepository.findAllByProduct(product)

        return ProductDetailResponse.fromEntity(product, productImages, productAiSummary)
    }
}