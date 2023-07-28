package com.example.autouserservice.controller;

import com.example.autouserservice.model.UserImplUserDetails;
import com.example.autouserservice.service.UserService;
import com.example.autouserservice.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtUtils jwtUtils;
    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<HashMap<String, String>>> registerUser(@RequestBody UserImplUserDetails user) {
        return userService.checkExistUser(user.getEmail())
                .flatMap(existingUser -> {
                    
                    if (existingUser.getUuid() != null) {
                        logger.info("Попытка регистрации с уже существующим email: email - {}", existingUser.getEmail());
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
                    } else {
                        return userService.save(user)
                                .cast(UserImplUserDetails.class)
                                .map(savedUser -> {
                                    logger.info("Пользователь с email - {} сохранен", savedUser.getEmail());
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("uuid", savedUser.getUuid());
                                    hashMap.put("token", jwtUtils.generateToken(savedUser));
                                    hashMap.put("name", savedUser.getUsername());
                                    return ResponseEntity.ok(hashMap);
                                });
                    }
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<HashMap<String, String>>> login(@RequestBody Map<String, String> loginRequest) {

        return userService.authenticateUser(loginRequest)
                .map(user -> {
                    UserImplUserDetails userDetails = (UserImplUserDetails) user;
                    String token = jwtUtils.generateToken(userDetails);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("uuid", userDetails.getUuid());
                    hashMap.put("token", token);
                    hashMap.put("name", userDetails.getUsername());
                    logger.info("user - {} - enter", userDetails.getEmail());
                    return ResponseEntity.ok(hashMap);
                });
    }

}
