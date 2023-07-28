package com.example.autoannouncement.security;

import com.example.autoannouncement.utils.JwtUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtils jwtTokenProvider;

    public JwtAuthenticationFilter(JwtUtils jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractTokenFromRequest(exchange.getRequest());

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        return chain.filter(exchange);
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {

        String token = null;
        String authHeader = request
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        return token;
    }
}
