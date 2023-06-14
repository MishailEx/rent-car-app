package com.example.autochatservice.config;

import com.example.autochatservice.controller.WebSocketHandler;
import com.example.autochatservice.mq.MessageActions;
import com.example.autochatservice.service.WebSocketManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MessageActions messageActions;
    private final WebSocketManager webSocketManager;

    public WebSocketConfig(MessageActions messageActions, WebSocketManager webSocketManager) {
        this.messageActions = messageActions;
        this.webSocketManager = webSocketManager;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(messageActions, webSocketManager), "/ws").setAllowedOrigins("*");
    }
}
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/queue");
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat").withSockJS();
//    }
//
//    @Override
//    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
//        registration.setMessageSizeLimit(1024 * 1024);
//    }
//}
