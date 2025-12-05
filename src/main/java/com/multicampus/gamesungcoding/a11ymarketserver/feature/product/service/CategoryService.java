package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.CategoryResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategories() {
        var categories = categoryRepository.findAll();

        Map<UUID, CategoryResponse> categoryMap = categories.stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toMap(
                        CategoryResponse::getCategoryId,
                        categoryResponse -> categoryResponse
                ));

        List<CategoryResponse> roots = new ArrayList<>();

        for (var category : categories) {
            var currentDto = categoryMap.get(category.getCategoryId());

            if (category.getParentCategory() == null) {
                roots.add(currentDto);
            } else {
                var parentId = category.getParentCategory().getCategoryId();
                var parentDto = categoryMap.get(parentId);

                if (parentDto != null) {
                    parentDto.addSubCategory(currentDto);
                }
            }
        }

        return roots;
    }
}
