package com.example.autoannouncement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
                .cors()
                .and()
                .authorizeExchange()
                .pathMatchers("/api/search/**", "/api/ann/all", "/api/ann/{id}").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.FIRST);

        return http.build();
    }
}
