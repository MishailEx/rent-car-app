package com.example.autorent.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class MessageFunc {

    @Bean
    public Consumer<Message<String>> actionConsume() {
        return stringMessage -> stringMessage.getPayload();
    }
}
