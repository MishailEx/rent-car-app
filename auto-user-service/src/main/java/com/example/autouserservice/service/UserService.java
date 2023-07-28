package com.example.autouserservice.service;

import com.example.autouserservice.model.UserImplUserDetails;
import com.example.autouserservice.model.UserRole;
import com.example.autouserservice.mq.MessageActions;
import com.example.autouserservice.repository.UserRepository;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService implements ReactiveUserDetailsService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private MessageActions messageActions;
    private AtomicInteger countUser = new AtomicInteger();


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MessageActions messageActions, MeterRegistry meterRegistry) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageActions = messageActions;
        meterRegistry.gauge("countUser", countUser);
    }

    public Mono<UserImplUserDetails> save(UserImplUserDetails user) {
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRole().add(UserRole.ROLE_USER);
        messageActions.sendEmail(user.getEmail());
        return userRepository.save(user).map(rslUser -> {
            countUser.getAndIncrement();
            return rslUser;
        });
    }

    public Mono<UserImplUserDetails> checkExistUser(String email) {
        return userRepository.findByEmail(email).switchIfEmpty(Mono.just(new UserImplUserDetails()));
    }

    @Timed
    public Mono<UserDetails> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        return userRepository.findByEmail(email)
                .flatMap(userDetails -> {
                    if (passwordEncoder.matches(password, userDetails.getPassword())) {
                        return Mono.just(userDetails);
                    } else {
                        return authError(email, password);
                    }
                });
    }

    private Mono<UserImplUserDetails> authError(String login, String password) {
        logger.warn("Попытка войти с неверным логином или паролем: логин - {} пароль - {}", login, password);
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @Timed
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username).cast(UserDetails.class);
    }
}
