package com.example.autouserservice.service;

import com.example.autouserservice.model.UserImplUserDetails;
import com.example.autouserservice.model.UserRole;
import com.example.autouserservice.mq.MessageActions;
import com.example.autouserservice.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Service
public class UserService implements ReactiveUserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private MessageActions messageActions;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MessageActions messageActions) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageActions = messageActions;
    }

    public Mono<UserImplUserDetails> save(UserImplUserDetails user) {
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRole().add(UserRole.ROLE_USER);
        messageActions.sendEmail(user.getEmail());
        return userRepository.save(user);
    }

    public Mono<UserImplUserDetails> checkExistUser(String email) {
        return userRepository.findByEmail(email).switchIfEmpty(Mono.empty());
    }

    public Mono<UserDetails> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        return userRepository.findByEmail(email)
                .filter(userDetails -> passwordEncoder.matches(password, userDetails.getPassword()))
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid credentials")))
                .map(userDetails -> userDetails);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username).cast(UserDetails.class);
    }
}
