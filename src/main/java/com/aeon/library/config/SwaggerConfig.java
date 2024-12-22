package com.aeon.library.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("aeon-library-assessment")
                .pathsToMatch("/api/books/**", "/api/borrowers/**")
                .build();
    }

    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info()
                        .title("AEON Library Assessment API")
                        .version("1.0")
                        .description("API documentation AEON Library Assessment API")
                        .contact(new Contact().name("Malik Asad").email("asdmnzr@gmail.com"))
                );
    }
}
