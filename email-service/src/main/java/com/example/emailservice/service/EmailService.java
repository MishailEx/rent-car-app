package com.example.emailservice.service;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class EmailService {

    @Bean
    public Consumer<Message<String>> consumeEmail() {
        return stringMessage -> System.out.println(stringMessage.getPayload());
    }
}
