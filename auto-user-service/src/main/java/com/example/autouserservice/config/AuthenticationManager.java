package com.example.autouserservice.config;

import com.example.autouserservice.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JwtUtils jwtUtils;

    public AuthenticationManager(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        String username;

        try {
           username = jwtUtils.extractUsername(authToken);
        } catch (Exception e) {
            username = null;
        }

        if (username != null && jwtUtils.validateToken(authToken)) {
            Claims claims = jwtUtils.getClaims(authToken);

            List<String> role = claims.get("role", List.class);
            List<SimpleGrantedAuthority> collect = role.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            return Mono.just(new UsernamePasswordAuthenticationToken(username, null, collect));
        }


        return Mono.empty();
    }
}
