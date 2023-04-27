package com.example.autorentuser.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.function.Supplier;

@Service
public class MessageFunc {

    private Sinks.Many<Message<Long>> bus = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE);

    @Bean
    public Supplier<Flux<Message<Long>>> newUserAction() {
        return () -> bus.asFlux();
    }

    public String newUser(Long id) {
        bus.emitNext(MessageBuilder.withPayload(id).build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return "message send";
    }
}
