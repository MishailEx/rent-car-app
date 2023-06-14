package com.example.autochatservice.service;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketManager {

    private Map<String, WebSocketSession> sessions = new HashMap<>();

    public void addUser(String userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void removeUser(String userId) {
        sessions.remove(userId);
    }

    public void sendMessage(String userId, String message) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }
}
