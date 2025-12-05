package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity;

import com.multicampus.gamesungcoding.a11ymarketserver.common.id.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainPageEvents {

    @Id
    @UuidV7
    @Column(length = 16, nullable = false, updatable = false)
    private UUID eventId;

    @Column(length = 200, nullable = false)
    private String eventTitle;

    @Column(length = 1000, nullable = false)
    private String eventDescription;

    @Column(length = 2048, nullable = false)
    private String eventImageUrl;

    @Column(length = 2048)
    private String eventUrl;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startDate;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime endDate;

    @Builder
    private MainPageEvents(String eventTitle,
                           String eventDescription,
                           String eventImageUrl,
                           String eventUrl,
                           LocalDateTime startDate,
                           LocalDateTime endDate) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventImageUrl = eventImageUrl;
        this.eventUrl = eventUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
