package com.meetime.hubspot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Case Técnico: Integração com HubSpot",
                version = "1.0.0",
                contact = @Contact(name = "Mateus Franco", email = "mateusfrancovinicius@gmail.com"),
                description = "Documentação da API que integra com o HubSpot"
        )
)
@Configuration
public class SwaggerConfig {
}