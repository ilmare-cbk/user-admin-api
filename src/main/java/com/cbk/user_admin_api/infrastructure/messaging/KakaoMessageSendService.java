package com.cbk.user_admin_api.infrastructure.messaging;

import com.cbk.user_admin_api.application.service.MessageEventProduceService;
import com.cbk.user_admin_api.infrastructure.messaging.dto.MessageEventDto;
import com.cbk.user_admin_api.infrastructure.ratelimit.RateLimiter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMessageSendService implements MessageSendService {
    private static final String MESSAGE = "%s님, 안녕하세요. 현대 오토에버입니다.";
    private final RateLimiter rateLimiter;
    private final WebClient webClient;
    private final MessageEventProduceService messageEventProduceService;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.kakao-message-retry}")
    private String kakaoMessageRetryTopic;

    @Value("${admin.kakao.message.id}")
    private String kakaoId;

    @Value("${admin.kakao.message.password}")
    private String kakaoPassword;

    @Value("${admin.kakao.message.url}")
    private String kakaoMessageUrl;


    @Override
    public boolean isSupported(MessageHandlerKey key) {
        return key.equals(MessageHandlerKey.KAKAO_MESSAGE);
    }

    @Override
    public void send(String message) {
        // rate limit 인 경우
        if (!rateLimiter.tryConsume("kakao_" + kakaoId, "kakao")) {
            retry(message);
            return;
        }

        // 카카오 메세지 전송 (외부 API 연동)
        MessageEventDto event = MessageEventDto.parseMessage(objectMapper, message);
        String auth = kakaoId + ":" + kakaoPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        Map<String, String> requestBody = Map.of("phone", event.getPhone(),
                                                 "message", String.format(MESSAGE, event.getName()) + System.lineSeparator() + event.getMessage());
        webClient.post()
                .uri(kakaoMessageUrl)
                .header("Authorization", "Basic " + encodedAuth)
                .bodyValue(requestBody)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(res -> log.info("카카오 메세지 발송 성공: {}", res.getStatusCode()))
                .doOnError(res -> retry(message))
                .subscribe();
    }

    private void retry(String message) {
        messageEventProduceService.produceEvent(kakaoMessageRetryTopic, message);
    }

}
