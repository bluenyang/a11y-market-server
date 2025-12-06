package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * 목록/검색용 Repository.
 * - JPQL 사용: DB 컬럼명 변경에 대한 내성 확보
 * - 문자열 연결은 CONCAT 중첩으로 '%search%' 구성 (JPQL 제약)
 */
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("""
            SELECT p
            FROM Product p
             LEFT JOIN FETCH p.seller
             LEFT JOIN FETCH p.category
             LEFT JOIN FETCH p.productImages
            WHERE (:search IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', CONCAT(:search, '%'))))
             AND p.productStatus = 'APPROVED'
            """)
    List<Product> findFilteredProducts(@Param("search") String search);

    @Query("""
             SELECT p
             FROM Product p
              LEFT JOIN FETCH p.seller
              LEFT JOIN FETCH p.category
              LEFT JOIN FETCH p.productImages
             WHERE p.productStatus = 'APPROVED'
            """)
    List<Product> findAllWithDetails();

    // 특정 판매자의 상품 전체 조회
    List<Product> findBySeller_SellerId(UUID sellerId);

    List<Product> findAllBySeller_User_UserEmail(String userEmail);
}
