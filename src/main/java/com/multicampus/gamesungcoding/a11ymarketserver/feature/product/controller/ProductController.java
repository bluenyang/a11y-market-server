package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}

