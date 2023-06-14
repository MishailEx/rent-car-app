package com.example.autoannouncement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
//                .pathMatchers("/api/search/**", "/api/ann/all", "/api/ann/{id}").permitAll()
                .anyExchange().permitAll()
                .and()
                .csrf().disable()
                .cors();

        return http.build();
    }

//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080")); // разрешаем только с домена localhost:8080
//        configuration.addAllowedMethod("*"); // разрешаем все HTTP-методы
//        configuration.addAllowedHeader("*"); // разрешаем все заголовки
//        configuration.setAllowCredentials(true); // разрешаем настройку withCredentials
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // настраиваем на все URL
//
//        return new CorsWebFilter(source);
//    }
}
