package com.example.autochatservice.mq;

import lombok.Getter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@Getter
public class MessageActions {

    private MessageFunc messageFunc;

    public MessageActions(MessageFunc messageFunc) {
        this.messageFunc = messageFunc;
    }

    public void sendMessage(String chatMessage) {
        messageFunc.getInnerBus().emitNext(MessageBuilder.withPayload(chatMessage).build(), Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
