package com.search.teacher.Techlearner.config;

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
    info = @Info(title = "Search Teacher"),
    security = @SecurityRequirement(name = "Authorization"),
    servers = {
        @Server(url = "http://localhost:6464", description = "Local Server"),
        @Server(url = "http://ec2-54-242-242-208.compute-1.amazonaws.com:6464", description = "Prod Server")
    }
)
public class SwaggerConfig {
}
