package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.CategoryRecommendResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.EventResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MonthlyPopularProduct;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {

    private final MainService mainService;

    @GetMapping("/v1/main/products/populars")
    public ResponseEntity<List<MonthlyPopularProduct>> getPopularProducts() {
        return ResponseEntity.ok(mainService.findTop10ByOrderByRankingAsc());
    }

    @GetMapping("/v1/main/products/categories")
    public ResponseEntity<List<CategoryRecommendResponse>> getCategories() {
        return ResponseEntity.ok(mainService.getAllCategories());
    }

    @GetMapping("/v1/main/events")
    public ResponseEntity<List<EventResponse>> getEvents() {
        return ResponseEntity.ok(mainService.getAllEvents());
    }
}
