package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProductImages {
    @Id
    @UuidV7
    @Column(length = 16, updatable = false, nullable = false)
    private UUID imageId;

    @Column(length = 16, nullable = false, updatable = false)
    private UUID productId;

    @Column(length = 2048, nullable = false)
    private String imageUrl;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String altText;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer imageSequence;

    @Builder
    private ProductImages(
            UUID productId,
            String imageUrl,
            String altText,
            Integer imageSequence) {

        this.productId = productId;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.imageSequence = imageSequence;
    }
}
