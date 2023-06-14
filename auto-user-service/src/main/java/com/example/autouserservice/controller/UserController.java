package com.example.autouserservice.controller;

import com.example.autouserservice.model.UserImplUserDetails;
import com.example.autouserservice.service.UserService;
import com.example.autouserservice.utils.JwtUtils;
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

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<HashMap<String, String>>> registerUser(@RequestBody UserImplUserDetails user) {
        return userService
                .checkExistUser(user.getEmail())
                .flatMap(existingUser -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)))
                .switchIfEmpty(userService.save(user))
                .cast(UserImplUserDetails.class)
                .map(savedUser -> {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("uuid", savedUser.getUuid());
                    hashMap.put("token", jwtUtils.generateToken(savedUser));
                    hashMap.put("name", savedUser.getUsername());
                    return ResponseEntity.ok(hashMap);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<HashMap<String, String>>> login(@RequestBody Map<String, String> loginRequest) {

        return userService.authenticateUser(loginRequest)
                .map(user -> {
                    String token = jwtUtils.generateToken((UserImplUserDetails) user);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("uuid", ((UserImplUserDetails) user).getUuid());
                    hashMap.put("token", token);
                    hashMap.put("name",  user.getUsername());
                    return ResponseEntity.ok(hashMap);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
//                .map(user -> {
//                    String token = jwtUtils.generateToken((UserImplUserDetails) user);
//                    return ResponseEntity.ok(token);
//                })
//                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}
