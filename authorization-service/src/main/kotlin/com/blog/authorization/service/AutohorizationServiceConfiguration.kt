package com.blog.authorization.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class AutohorizationServiceConfiguration {
    @Bean
    fun inMemoryUserDetailsManager(): InMemoryUserDetailsManager? {
        val one = User.withDefaultPasswordEncoder().roles("admin").username("admin").password("pw").build()
        val two = User.withDefaultPasswordEncoder().roles("user").username("user").password("pw").build()
        return InMemoryUserDetailsManager(one, two)
    }
}