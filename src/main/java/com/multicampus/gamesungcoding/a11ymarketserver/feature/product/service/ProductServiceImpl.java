package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * - search 파라미터 유무에 따라 전체/필터 조회
 * - certified/grade는 스펙 확장 시 반영
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductDTO> getProducts(String search, Boolean certified, String grade) {
        final List<Product> products =
                (search == null || search.isBlank())
                        ? productRepository.findAll()
                        : productRepository.findFilteredProducts(search);

        return products.stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }
}


