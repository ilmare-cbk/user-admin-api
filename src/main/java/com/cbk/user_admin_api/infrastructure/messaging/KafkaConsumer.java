package com.cbk.user_admin_api.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final List<MessageSendService> messageSendServices;

    @KafkaListener(
            topics = "${kafka.topics.kakao-message}",
            groupId = "${kafka.consumer-groups.kakao-message}",
            containerFactory = "kakaoConsumerFactory")
    public void consumeKakao(String message) {
        MessageSendService messageSendService = getMessageSendService(MessageHandlerKey.KAKAO_MESSAGE);
        messageSendService.send(message);
    }

    @KafkaListener(
            topics = "${kafka.topics.kakao-message-retry}",
            groupId = "${kafka.consumer-groups.kakao-message-retry}",
            containerFactory = "kakaoRetryConsumerFactory")
    public void consumeRetryKakao(String message) {
        MessageSendService messageSendService = getMessageSendService(MessageHandlerKey.KAKAO_MESSAGE_RETRY);
        messageSendService.send(message);
    }

    @KafkaListener(
            topics = "${kafka.topics.sms}",
            groupId = "${kafka.consumer-groups.sms}",
            containerFactory = "smsConsumerFactory")
    public void consumeSms(String message) {
        MessageSendService messageSendService = getMessageSendService(MessageHandlerKey.SMS);
        messageSendService.send(message);
    }

    private MessageSendService getMessageSendService(MessageHandlerKey key) {
        return messageSendServices.stream()
                .filter(it -> it.isSupported(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MessageSendService not found"));
    }
}
