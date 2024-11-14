package com.outsera.razzies.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API RAZZIES",
                description = "API RESTful que permite acessar a lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards (Razzies).",
                version = "1.0.0"
        )
)
public class OpenAPIConfig {
    // Esta classe configura as informações do Swagger/OpenAPI
}