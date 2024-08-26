package com.example.rest_with_spring_boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;



@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("RESTful API with Java and Spring Boot")
                .version("v1")
                .description("Some description about your API.")
                .termsOfService("https://example.com/random")
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("https://example.com/random/xyz123")
                )
            );
    }
}
