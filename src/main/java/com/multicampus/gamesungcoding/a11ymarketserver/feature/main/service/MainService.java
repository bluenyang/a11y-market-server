package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.CatProductInfo;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.CategoryRecommendResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.EventResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MonthlyPopularProduct;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository.MainPageEventsRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository.MonthlyPopularProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository.RecommendationRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MainService {
    private final MonthlyPopularProductRepository monthlyPopularProductRepository;
    private final RecommendationRepository recommendationRepository;
    private final CategoryRepository categoryRepository;
    private final MainPageEventsRepository mainPageEventsRepository;

    public List<MonthlyPopularProduct> findTop10ByOrderByRankingAsc() {
        return monthlyPopularProductRepository.findTop10ByOrderByRankingAsc();
    }

    public List<CategoryRecommendResponse> getAllCategories() {
        var list = recommendationRepository.findAll();

        var roots = categoryRepository.findAllByParentCategoryIsNull()
                .stream()
                .map(CategoryRecommendResponse::fromEntity)
                .collect(Collectors.toMap(
                        CategoryRecommendResponse::getCategoryId,
                        categoryResponse -> categoryResponse
                ));

        for (var item : list) {
            var currentDto = roots.get(item.getRootCategoryId());
            if (currentDto != null) {
                var productInfo = new CatProductInfo(
                        item.getProductId(),
                        item.getProductName(),
                        item.getProductPrice(),
                        item.getProductImageUrl()
                );
                currentDto.addProduct(productInfo);
            }
        }

        return roots.values().stream().toList();
    }

    public List<EventResponse> getAllEvents() {
        var list = mainPageEventsRepository.findAllByStartDateBeforeAndEndDateAfterOrderByEventIdAsc(
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return list.stream()
                .map(EventResponse::fromEntity)
                .toList();
    }
}
