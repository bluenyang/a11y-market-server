package com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model;

import jakarta.validation.constraints.*;

public record UserA11yUpdateRequest(

        @NotNull
        @Min(0)
        @Max(3)
        Integer contrastLevel,

        @NotNull
        @Min(0)
        @Max(2)
        Integer textSizeLevel,

        @NotNull
        @Min(0)
        @Max(2)
        Integer textSpacingLevel,

        @NotNull
        @Min(0)
        @Max(2)
        Integer lineHeightLevel,

        @NotBlank
        @Pattern(regexp = "^(left|center|right)$")
        String textAlign,

        @NotNull
        Boolean screenReader,

        @NotNull
        Boolean smartContrast,

        @NotNull
        Boolean highlightLinks,

        @NotNull
        Boolean cursorHighlight

) {
}
