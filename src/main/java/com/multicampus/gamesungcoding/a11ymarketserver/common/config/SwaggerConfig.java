package com.multicampus.gamesungcoding.a11ymarketserver.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "A11yMARKET Server API",
                description = "A11yMARKET Server API Specification Document",
                version = "v0.0.1"
        ),
        security = {
                @SecurityRequirement(name = "JWT")
        }
)
@SecuritySchemes({
        @SecurityScheme(
                name = "JWT",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT",
                description = "JWT 토큰을 입력하세요."
        )
})
public class SwaggerConfig {
}
