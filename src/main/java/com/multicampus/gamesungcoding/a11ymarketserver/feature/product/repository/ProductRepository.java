package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.Product;
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
            WHERE (:search IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', CONCAT(:search, '%'))))
            """)
    List<Product> findFilteredProducts(@Param("search") String search);

    // certified/grade는 추후 Seller 조인 시 Specification/Join으로 확장 예정
}
