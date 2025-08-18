package com.cbk.user_admin_api.infrastructure.messaging;

import com.cbk.user_admin_api.application.service.MessageEventProduceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageEventProduceServiceImpl implements MessageEventProduceService {
    private final KafkaProducer kafkaProducer;

    @Override
    public void produceEvent(String topic, String event) {
        kafkaProducer.publish(topic, event);
    }

    @Override
    public void produceEvents(String topic, List<String> events) {
        events.forEach(event -> kafkaProducer.publish(topic, event));
    }
}
