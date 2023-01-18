package com.example.apiserver.rest

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class WebClientConfiguration {

    @Bean
    fun restTemplate(
        builder: RestTemplateBuilder
    ): RestTemplate =  builder.build()
}