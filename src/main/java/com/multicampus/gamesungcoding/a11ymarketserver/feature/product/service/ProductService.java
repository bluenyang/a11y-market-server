package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductAiSummaryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductImagesRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * - search 파라미터 유무에 따라 전체/필터 조회
 * - certified/grade는 스펙 확장 시 반영
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ProductAiSummaryRepository productAiSummaryRepository;

    @Transactional(readOnly = true)
    public List<ProductDTO> getProducts(String search, Boolean certified, String grade) {
        final List<Product> products =
                (search == null || search.isBlank())
                        ? productRepository.findAll()
                        : productRepository.findFilteredProducts(search);

        return products.stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(UUID productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Invalid product ID: " + productId));

        var productImages = productImagesRepository.findAllByProduct(product);
        var productAiSummary = productAiSummaryRepository.findAllByProduct(product);

        return ProductDetailResponse.fromEntity(product, productImages, productAiSummary);
    }
}