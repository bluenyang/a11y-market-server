package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/v1/products")
    public List<ProductDTO> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean certified,
            @RequestParam(required = false) String grade) {
        return productService.getProducts(search, certified, grade);
    }
}

