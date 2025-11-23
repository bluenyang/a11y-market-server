package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "USER_A11Y_SETTINGS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserA11ySettings {

    @Id
    @Column(name = "USER_ID", nullable = false, updatable = false, length = 16)
    private UUID userId;

    @Column(name = "CONTRAST_LEVEL", nullable = false)
    private Integer contrastLevel;

    @Column(name = "TEXT_SIZE_LEVEL", nullable = false)
    private Integer textSizeLevel;

    @Column(name = "TEXT_SPACING_LEVEL", nullable = false)
    private Integer textSpacingLevel;

    @Column(name = "LINE_HEIGHT_LEVEL", nullable = false)
    private Integer lineHeightLevel;

    @Column(name = "TEXT_ALIGN", length = 10, nullable = false)
    private String textAlign;

    @Column(name = "SCREEN_READER", nullable = false)
    private Integer screenReader;

    @Column(name = "SMART_CONTRAST", nullable = false)
    private Integer smartContrast;

    @Column(name = "HIGHLIGHT_LINKS", nullable = false)
    private Integer highlightLinks;

    @Column(name = "CURSOR_HIGHLIGHT", nullable = false)
    private Integer cursorHighlight;

    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public void updateSettings(
            Integer contrastLevel,
            Integer textSizeLevel,
            Integer textSpacingLevel,
            Integer lineHeightLevel,
            String textAlign,
            Integer screenReader,
            Integer smartContrast,
            Integer highlightLinks,
            Integer cursorHighlight
    ) {
        this.contrastLevel = contrastLevel;
        this.textSizeLevel = textSizeLevel;
        this.textSpacingLevel = textSpacingLevel;
        this.lineHeightLevel = lineHeightLevel;
        this.textAlign = textAlign;
        this.screenReader = screenReader;
        this.smartContrast = smartContrast;
        this.highlightLinks = highlightLinks;
        this.cursorHighlight = cursorHighlight;
    }
}
