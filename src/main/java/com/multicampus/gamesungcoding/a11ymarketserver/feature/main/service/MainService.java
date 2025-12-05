package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.model.MonthlyPopularProduct;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository.MonthlyPopularProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {
    private final MonthlyPopularProductRepository monthlyPopularProductRepository;

    public List<MonthlyPopularProduct> findTop10ByOrderByRankingAsc() {
        return monthlyPopularProductRepository.findTop10ByOrderByRankingAsc();
    }
}
