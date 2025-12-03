package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.UserA11yProfile;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserA11yProfileResponse(
        UUID profileId,
        String profileName,
        String description,
        Integer contrastLevel,
        Integer textSizeLevel,
        Integer textSpacingLevel,
        Integer lineHeightLevel,
        String textAlign,
        Boolean screenReader,
        Boolean smartContrast,
        Boolean highlightLinks,
        Boolean cursorHighlight,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static UserA11yProfileResponse fromEntity(UserA11yProfile p) {
        var pi = p.getProfileInfo();
        return new UserA11yProfileResponse(
                p.getProfileId(),
                pi.getProfileName(),
                pi.getDescription(),
                pi.getContrastLevel(),
                pi.getTextSizeLevel(),
                pi.getTextSpacingLevel(),
                pi.getLineHeightLevel(),
                pi.getTextAlign(),
                pi.getScreenReader(),
                pi.getSmartContrast(),
                pi.getHighlightLinks(),
                pi.getCursorHighlight(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
