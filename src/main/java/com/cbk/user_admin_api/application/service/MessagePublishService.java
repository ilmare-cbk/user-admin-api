package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.command.MessageCommand;
import com.cbk.user_admin_api.application.exception.ApplicationException;
import com.cbk.user_admin_api.application.query.Message;
import com.cbk.user_admin_api.domain.UserQueryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePublishService {
    @Value("${kafka.topics.kakao-message}")
    private String topic;

    private final UserQueryRepository userQueryRepository;
    private final ObjectMapper objectMapper;
    private final MessageEventProduceService messageEventProduceService;

    public void publishMessage(MessageCommand command) {
        List<String> messageEvents = userQueryRepository.findAllByAgeGroup(command.getAgeGroup())
                .stream()
                .map(it -> {
                    try {
                        return objectMapper.writeValueAsString(Message.of(it.getName(), it.getPhoneNumber(), command.getMessage()));
                    } catch (JsonProcessingException e) {
                        log.error("", e);
                        throw new ApplicationException("Failed to send message");
                    }
                })
                .toList();

        messageEventProduceService.produceEvents(topic, messageEvents);
    }
}
