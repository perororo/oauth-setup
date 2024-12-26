package com.blog.oauth.client

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ClientConfiguration {
    @Bean
    fun gateway(rlb: RouteLocatorBuilder): RouteLocator? {
        return rlb
            .routes()
            .route { rs: PredicateSpec ->
                rs
                    .path("/**")
                    .filters(GatewayFilterSpec::tokenRelay)
                    .uri("http://localhost:8081/")
            }
            .build()
    }
}