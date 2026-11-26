package com.project.fitness.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customAPI() {
        return new OpenAPI().info(new Info()
                .title("Fitness Monolith API")
                .version("v1.0")
                .description("Production grade APIs")
                .contact(new Contact()
                        .name("Daksh Kumar")
                        .email("daksh.kumar.ug22@gmail.com")
                        .url("https://daksh-kumar.netlify.app")
                )
        );
    }
}
