package com.search.teacher.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "Authorization",
        bearerFormat = "jwt",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(title = "Mock exam API"),
        security = @SecurityRequirement(name = "Authorization"),
        servers = {
                @Server(url = "http://localhost:6464", description = "Local Server"),
                @Server(url = "https://api.everestexams.uz", description = "Production Server")
        }
)
public class SwaggerConfig {
}
