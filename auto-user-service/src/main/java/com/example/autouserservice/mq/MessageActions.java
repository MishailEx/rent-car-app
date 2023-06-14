package com.example.autouserservice.mq;

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

    public void sendEmail(String email) {
        messageFunc.getInnerBus().emitNext(MessageBuilder.withPayload(email).build(), Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
