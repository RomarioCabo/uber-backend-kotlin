package com.br.uberclonekotlin.infrastructure.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun postsalesEnqueuerApi(): OpenAPI = OpenAPI().info(
        Info().title("UBER REST API")
            .description("erviço responsável pelo UBER clone")
            .version("v0.0.1")
    )
}