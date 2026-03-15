package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class A11yProfileInfo {

    @Column(nullable = false, length = 50)
    private String profileName;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private Integer contrastLevel;

    @Column(nullable = false)
    private Integer textSizeLevel;

    @Column(nullable = false)
    private Integer textSpacingLevel;

    @Column(nullable = false)
    private Integer lineHeightLevel;

    @Column(length = 10, nullable = false)
    private String textAlign;

    @Column(nullable = false)
    private Boolean screenReader;

    @Column(nullable = false)
    private Boolean smartContrast;

    @Column(nullable = false)
    private Boolean highlightLinks;

    @Column(nullable = false)
    private Boolean cursorHighlight;
}
