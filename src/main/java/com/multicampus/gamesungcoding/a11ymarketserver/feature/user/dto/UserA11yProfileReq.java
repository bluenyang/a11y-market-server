package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.dto;

import jakarta.validation.constraints.*;

public record UserA11yProfileReq(

        @NotBlank(message = "프로필 이름은 필수 입력 값입니다.")
        @Size(max = 50)
        String profileName,

        @Size(max = 200)
        String description,

        @Min(0) @Max(3)
        Integer contrastLevel,

        @Min(0) @Max(2)
        Integer textSizeLevel,

        @Min(0) @Max(2)
        Integer textSpacingLevel,

        @Min(0) @Max(2)
        Integer lineHeightLevel,

        @Pattern(regexp = "^(left|center|right)$", message = "텍스트 정렬은 left, center, right 중 하나여야 합니다.")
        String textAlign,

        Boolean screenReader,

        Boolean smartContrast,

        Boolean highlightLinks,

        Boolean cursorHighlight

) {
}
