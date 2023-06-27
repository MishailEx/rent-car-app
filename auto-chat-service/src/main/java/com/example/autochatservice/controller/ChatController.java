package com.example.autochatservice.controller;

import com.example.autochatservice.entity.ChatMessage;
import com.example.autochatservice.entity.ChatRequest;
import com.example.autochatservice.entity.ChatRoom;
import com.example.autochatservice.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/createChat")
    public ChatRoom createChat(@RequestBody ChatRequest chatRequest) {
        return chatService.getChat(chatRequest);
    }

    @PostMapping("/findChat")
    public ChatRoom findChat(@RequestBody ChatRequest chatRequest) {
        Optional<ChatRoom> chat = chatService.findChat(chatRequest);
        return chat.orElse(null);
    }


    @PostMapping("/getChatsByUser")
    public ResponseEntity<List<ChatRoom>> getChatsByUser(@RequestBody Map<String, String> uuid) {
        Optional<List<ChatRoom>> chat = chatService.findChats(uuid);
        if (chat.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(chat.get());
    }

    @PostMapping("/getChat")
    public ResponseEntity<List<ChatMessage>> getChat(@RequestBody Map<String, String> chatId) {
        long parseLong = Long.parseLong(chatId.get("chatId"));
        return ResponseEntity.ok(chatService.getChatMessages(parseLong));
    }
}
