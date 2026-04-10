package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ImageMetadata;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

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
    private ProductImages(Product product,
                          String imageUrl,
                          String altText,
                          Integer imageSequence) {

        this.product = product;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.imageSequence = imageSequence;
    }

    public void updateMetadata(ImageMetadata metadata) {
        this.altText = metadata.altText;
        this.imageSequence = metadata.sequence;
    }
}
