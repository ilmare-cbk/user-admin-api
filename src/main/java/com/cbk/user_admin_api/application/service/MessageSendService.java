package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.query.PhoneNumber;
import com.cbk.user_admin_api.domain.UserQueryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSendService {
    private final UserQueryRepository userQueryRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String MAIN_STREAM = "MAIN_STREAM";

    public void enqueueMessages(int ageGroup) {
        List<PhoneNumber> phones = userQueryRepository.findAllByAgeGroup(ageGroup)
                .stream()
                .map(it -> PhoneNumber.of(it.getName(), it.getPhoneNumber()))
                .toList();

        phones.forEach(phone -> {
            try {
                String json = objectMapper.writeValueAsString(phone);
                redisTemplate.opsForStream().add(MAIN_STREAM, Map.of("msg", json));
            } catch (JsonProcessingException e) {
                log.error("스트림 삽입 실패: {}", phone, e);
            }
        });
    }
}
