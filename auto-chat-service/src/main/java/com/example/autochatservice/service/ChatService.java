package com.example.autochatservice.service;

import com.example.autochatservice.entity.ChatMessage;
import com.example.autochatservice.entity.ChatRequest;
import com.example.autochatservice.entity.ChatRoom;
import com.example.autochatservice.repository.ChatMessageRepository;
import com.example.autochatservice.repository.ChatRoomRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ChatService {

    private Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatService(ChatMessageRepository chatRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    public ChatRoom getChat(ChatRequest request) {
        String uuidSender = request.uuid();
        String uuidRecipient = request.uuidRecipient();
        String name = request.name();

        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(uuidSender, uuidRecipient);
        if (chatRoom.isEmpty()) {
            ChatRoom createdChat = chatRoomRepository.save(new ChatRoom(name, uuidSender, uuidRecipient));
            logger.info("создание чата для пользователей: {} - {}", uuidRecipient, uuidSender);
            return createdChat;
        }
        return chatRoom.get();
    }

    public Optional<ChatRoom> findChat(ChatRequest request) {
        return chatRoomRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(request.uuid(), request.uuidRecipient());
    }

    public List<ChatMessage> getChatMessages(Long chatId) {
        return chatMessageRepository.findAllByChatRoomId(chatId);
    }

    @Bean
    public Consumer<Message<String>> takeMessage() {
        return stringMessage -> {
            JSONObject jsonObject = new JSONObject(stringMessage.getPayload());
            ChatMessage chatMessage = new ChatMessage(jsonObject.getString("message"),
                    LocalDateTime.parse(jsonObject.getString("timestamp"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    jsonObject.getString("userName"), jsonObject.getLong("chatRoomId"));
            chatMessageRepository.save(chatMessage);
        };
    }

    public Optional<List<ChatRoom>> findChats(Map<String, String> uuid) {
        String uuidFind = uuid.get("uuid");
        return chatRoomRepository.findAllBySenderIdOrRecipientId(uuidFind, uuidFind);
    }
}
