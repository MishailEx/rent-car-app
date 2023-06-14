package com.example.autouserservice.config;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private String springSecurityContextAttrName = "SPRING_SECURITY_CONTEXT";
    private AuthenticationManager authenticationManager;

    public SecurityContextRepository(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {

        return exchange.getSession().doOnNext((session) -> {
            if (context == null) {
                session.getAttributes().remove(this.springSecurityContextAttrName);
            } else {
                session.getAttributes().put(this.springSecurityContextAttrName, context);
            }

        }).flatMap(WebSession::changeSessionId);
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwt, jwt);
            return authenticationManager
                    .authenticate(auth)
                    .map(SecurityContextImpl::new);
        }
        return Mono.empty();
    }
}
