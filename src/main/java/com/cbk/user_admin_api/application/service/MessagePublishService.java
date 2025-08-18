package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.query.PhoneNumber;
import com.cbk.user_admin_api.domain.UserQueryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessagePublishService {
    @Value("${kafka.topics.kakao-message}")
    private String topic;

    private final UserQueryRepository userQueryRepository;
    private final ObjectMapper objectMapper;
    private final MessageEventProduceService messageEventProduceService;

    public void publishMessage(int ageGroup) {
        List<String> messageEvents = userQueryRepository.findAllByAgeGroup(ageGroup)
                .stream()
                .map(it -> {
                    try {
                        return objectMapper.writeValueAsString(PhoneNumber.of(it.getName(), it.getPhoneNumber()));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        messageEventProduceService.produceEvents(topic, messageEvents);
    }
}
