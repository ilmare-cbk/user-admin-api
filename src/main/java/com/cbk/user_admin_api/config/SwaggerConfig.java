package com.cbk.user_admin_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        // JWT 설정
                        .addSecuritySchemes("JWT",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .name("JWT")
                                        .type(Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(In.HEADER)
                        )
                        // Basic Auth 설정
                        .addSecuritySchemes("basicAuth",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .name("BASIC")
                                        .type(Type.HTTP)
                                        .scheme("basic")
                                        .in(In.HEADER)
                        )
                );
    }
}
