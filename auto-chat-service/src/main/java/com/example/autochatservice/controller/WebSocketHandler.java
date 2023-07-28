package com.example.autochatservice.controller;

import com.example.autochatservice.entity.ChatMessage;
import com.example.autochatservice.mq.MessageActions;
import com.example.autochatservice.service.WebSocketManager;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final MessageActions messageActions;

    private final WebSocketManager webSocketManager;

    public WebSocketHandler(MessageActions messageActions, WebSocketManager webSocketManager) {
        this.messageActions = messageActions;
        this.webSocketManager = webSocketManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String uuid = session.getUri().getQuery();
        webSocketManager.addUser(uuid, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage messageChat) throws Exception {
        String receivedMessage = messageChat.getPayload();
        JSONObject jsonObject = new JSONObject(receivedMessage);
        String uuid = jsonObject.getJSONObject("uuids").getString("uuid");
        String uuidRecipient = jsonObject.getJSONObject("uuids").getString("uuidRecipient");
        String message = jsonObject.getString("message");
        Long chatId = jsonObject.getBigInteger("chatId").longValue();
        String name = jsonObject.getString("name");
        LocalDateTime now = LocalDateTime.now();

        ChatMessage chatMessage = new ChatMessage(message, now, name, chatId);

        JSONObject jsonMessage = new JSONObject(chatMessage);

        webSocketManager.sendMessage(uuid, jsonMessage.toString());
        webSocketManager.sendMessage(uuidRecipient, jsonMessage.toString());

        messageActions.sendMessage(jsonMessage.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String uuid = session.getUri().getQuery();
        webSocketManager.removeUser(uuid);
    }
}