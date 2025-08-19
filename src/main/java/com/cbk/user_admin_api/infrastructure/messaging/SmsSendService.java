package com.cbk.user_admin_api.infrastructure.messaging;

import com.cbk.user_admin_api.infrastructure.persistence.MessageFailEntity;
import com.cbk.user_admin_api.infrastructure.persistence.MessageFailJpaRepository;
import com.cbk.user_admin_api.infrastructure.persistence.MessageType;
import com.cbk.user_admin_api.infrastructure.messaging.dto.MessageEventDto;
import com.cbk.user_admin_api.infrastructure.ratelimit.RateLimiter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsSendService implements MessageSendService {
    private static final String MESSAGE = "%s님, 안녕하세요. 현대 오토에버입니다.";

    private final RateLimiter rateLimiter;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final MessageFailJpaRepository messageFailJpaRepository;

    @Value("${kafka.topics.sms}")
    private String smsTopic;

    @Value("${admin.sms.message.id}")
    private String smsId;

    @Value("${admin.sms.message.password}")
    private String smsPassword;

    @Value("${admin.sms.message.url}")
    private String smsMessageUrl;

    @Override
    public boolean isSupported(MessageHandlerKey key) {
        return key.equals(MessageHandlerKey.SMS);
    }

    @Override
    public void send(String message) {
        MessageEventDto event = MessageEventDto.parseMessage(objectMapper, message);

        // rate limit 인 경우
        if (!rateLimiter.tryConsume("sms_" + smsId, "sms")) {
            saveFailMessage(event);
            return;
        }

        String auth = smsId + ":" + smsPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        webClient.post()
                .uri(UriComponentsBuilder.fromUriString(smsMessageUrl)
                             .queryParam("phone", event.getPhone())
                             .build()
                             .toUriString())
                .header("Authorization", "Basic " + encodedAuth)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("message", String.format(MESSAGE, event.getName())))
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(res -> log.info("sms 메세지 발송 성공: {}", res.getStatusCode()))
                .doOnError(res -> saveFailMessage(event))
                .subscribe();
    }

    private void saveFailMessage(MessageEventDto event) {
        messageFailJpaRepository.save(
                MessageFailEntity.create(MessageType.SMS,
                                         Map.of("phone", event.getPhone(), "message", String.format(MESSAGE, event.getName())).toString())
        );
    }
}
