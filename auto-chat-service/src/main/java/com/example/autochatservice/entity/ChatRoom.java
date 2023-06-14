package com.example.autochatservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@RequiredArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String senderId;
    private String recipientId;

    @OneToMany
    @JoinColumn(name = "chat_room_id")
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatRoom(String name, String senderId, String recipientId) {
        this.name = name;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return id != null && Objects.equals(id, chatRoom.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
