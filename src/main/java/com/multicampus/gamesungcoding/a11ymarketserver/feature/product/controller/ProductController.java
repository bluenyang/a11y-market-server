package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/v1/products")
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean certified,
            @RequestParam(required = false) String grade) {
        return ResponseEntity.ok(
                productService.getProducts(search, certified, grade));
    }

    @GetMapping("/v1/products/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
            @PathVariable String productId) {
        return ResponseEntity.ok(
                productService.getProductDetail(UUID.fromString(productId)));
    }
}

