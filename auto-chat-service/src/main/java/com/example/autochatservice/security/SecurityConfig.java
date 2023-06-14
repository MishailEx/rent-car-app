//package com.example.autochatservice.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests()
////                .pathMatchers("/api/search/**", "/api/ann/all", "/api/ann/{id}").permitAll()
//                .anyRequest().permitAll()
//                .and()
//                .csrf().disable()
//                .cors();
//
//        return http.build();
//    }
//
////    @Bean
////    public CorsFilter corsWebFilter() {
////        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080")); // разрешаем только с домена localhost:8080
////        configuration.addAllowedMethod("*"); // разрешаем все HTTP-методы
////        configuration.addAllowedHeader("*"); // разрешаем все заголовки
////        configuration.setAllowCredentials(true); // разрешаем настройку withCredentials
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", configuration); // настраиваем на все URL
////
////        return new CorsWebFilter(source);
////    }
////
////    @Bean
////    public CorsConfigurationSource corsConfigurationSource() {
////        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.addAllowedOrigin("*"); // Разрешаем запросы из всех источников
////        configuration.addAllowedMethod("*"); // Разрешаем все HTTP методы
////        configuration.addAllowedHeader("*"); // Разрешаем все заголовки
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", configuration); // Применяем конфигурацию для всех URL-ов
////
////        return new CorsConfiguration(source);
////    }
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("*").allowedOrigins("http://localhost:8080");
//            }
//        };
//    }
//}
