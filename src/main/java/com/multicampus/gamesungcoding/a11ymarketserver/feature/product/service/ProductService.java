package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductDTO;

import java.util.List;

/**
 * 목록/검색 통합 서비스.
 * - search/certified/grade 중 필요한 값만 전달
 */
public interface ProductService {
    List<ProductDTO> getProducts(String search, Boolean certified, String grade);
}