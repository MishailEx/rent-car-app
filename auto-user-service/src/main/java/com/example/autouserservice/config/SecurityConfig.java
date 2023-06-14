package com.example.autouserservice.config;

import com.example.autouserservice.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {

    private final JwtUtils jwtTokenUtil;
    private AuthenticationManager authenticationManager;

    private SecurityContextRepository securityContextRepository;

    public SecurityConfig(JwtUtils jwtTokenUtil, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return exchange -> {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedMethod("GET");
            corsConfiguration.addAllowedMethod("POST");
            corsConfiguration.addAllowedHeader("Authorization");
            corsConfiguration.addAllowedHeader("Content-Type");
            corsConfiguration.setAllowCredentials(true);

            source.registerCorsConfiguration("/**", corsConfiguration);
            return corsConfiguration;
        };
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(authorizeExchangeSpec -> {
                    authorizeExchangeSpec.pathMatchers("/", "/login", "/logout", "/register", "/favicon.ico").permitAll()
                            .anyExchange().authenticated();
                })
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((exchange, authentication) -> {
                            ServerHttpResponse response = exchange.getExchange().getResponse();
                            response.setStatusCode(HttpStatus.OK);
                            return response.setComplete();
                        }))
                .build();
    }
}
