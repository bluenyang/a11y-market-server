package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.model.MonthlyPopularProduct;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.service.MainService;
import lombok.RequiredArgsConstructor;
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
    public List<MonthlyPopularProduct> getPopularProducts() {
        return mainService.findTop10ByOrderByRankingAsc();
    }
}
