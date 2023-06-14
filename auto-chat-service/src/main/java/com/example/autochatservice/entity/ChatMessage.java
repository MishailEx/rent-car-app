package com.example.autochatservice.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDateTime timestamp;
    private String userName;

    @Column(name = "chat_room_id")
    private Long chatRoomId;

    public ChatMessage(String message, LocalDateTime timestamp, String userName, Long chatRoomId) {
        this.message = message;
        this.timestamp = timestamp;
        this.userName = userName;
        this.chatRoomId = chatRoomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatMessage that = (ChatMessage) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}