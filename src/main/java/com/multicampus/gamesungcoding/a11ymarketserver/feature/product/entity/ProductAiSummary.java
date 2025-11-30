package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProductAiSummary {
    @Id
    @Column(length = 16, updatable = false, nullable = false)
    private UUID productId;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String summaryText;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String usageContext;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String usageMethod;

    @CreatedDate
    @Column(updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime generatedAt;

    @Builder
    private ProductAiSummary(UUID productId,
                             String summaryText,
                             String usageContext,
                             String usageMethod) {

        this.productId = productId;
        this.summaryText = summaryText;
        this.usageContext = usageContext;
        this.usageMethod = usageMethod;
    }
}
