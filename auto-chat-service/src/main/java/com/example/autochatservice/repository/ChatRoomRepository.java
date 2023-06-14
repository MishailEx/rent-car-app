package com.example.autochatservice.repository;

import com.example.autochatservice.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT c FROM ChatRoom c WHERE (c.senderId = :senderId AND c.recipientId = :recipientId) OR (c.senderId = :recipientId AND c.recipientId = :senderId)")
    Optional<ChatRoom> findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(String senderId, String recipientId);
    Optional<List<ChatRoom>> findAllBySenderIdOrRecipientId(String uuid, String uuid2);
}
