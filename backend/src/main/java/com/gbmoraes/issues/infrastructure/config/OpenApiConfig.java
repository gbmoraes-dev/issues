package com.gbmoraes.issues.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
  name = "bearer-token",
  type = SecuritySchemeType.HTTP,
  scheme = "bearer",
  bearerFormat = "JWT"
)
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    Server server = new Server();
    server.setUrl("/");
    server.setDescription("Current server");
    return new OpenAPI()
      .info(new Info()
        .title("Issues API")
        .version("v1")
        .description("REST API for managing personal issues with JWT authentication"))
      .servers(List.of(server));
  }
}
