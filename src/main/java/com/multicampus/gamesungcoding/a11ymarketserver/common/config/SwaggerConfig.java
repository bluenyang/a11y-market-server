package com.multicampus.gamesungcoding.a11ymarketserver.common.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        var securityRequirement = new SecurityRequirement().addList(jwt);
        var components = new Components()
                .addSecuritySchemes(
                        jwt,
                        new SecurityScheme()
                                .name(jwt)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );

        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("A11y Market Server API")
                        .description("A11y Market Server API 명세서입니다.")
                        .version("v0.0.1")
                );
    }
}
