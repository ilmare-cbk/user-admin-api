package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.query.PhoneNumber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageScheduler {
    private final RateLimiterService rateLimiterService;
    private final WebClient webClient;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${admin.kakao.message.id}")
    private String kakaoId;

    @Value("${admin.kakao.message.password}")
    private String kakaoPassword;

    @Value("${admin.kakao.message.url}")
    private String kakaoMessageUrl;

    @Value("${admin.sms.message.id}")
    private String smsId;

    @Value("${admin.sms.message.password}")
    private String smsPassword;

    @Value("${admin.sms.message.url}")
    private String smsMessageUrl;

    private final ObjectMapper objectMapper;
    private static final String MESSAGE = "%s님, 안녕하세요. 현대 오토에버입니다.";
    private static final String MAIN_STREAM = "MAIN_STREAM";
    private static final String RETRY_STREAM = "RETRY_STREAM";

    @Scheduled(fixedRate = 30000)
    public void send() {
        processMainStream();
        processRetryStream();
    }

    private void processMainStream() {
        List<MapRecord<String, String, String>> records =
                redisTemplate.<String, String>opsForStream()
                        .read(StreamReadOptions.empty().count(100),
                              StreamOffset.fromStart(MAIN_STREAM));

        if (records == null) return;

        Flux.fromIterable(records)
                .concatMap(record -> {
                    String json = record.getValue().get("msg");
                    try {
                        PhoneNumber phone = objectMapper.readValue(json, PhoneNumber.class);
                        return sendKakaoMessage(phone)
                                .doOnSuccess(v -> redisTemplate.opsForStream().delete(MAIN_STREAM, record.getId()))
                                .onErrorResume(err -> {
                                    log.error("{}: 전송 실패, 재시도 큐 이동: {}", MAIN_STREAM, phone.getName());
                                    redisTemplate.opsForStream().delete(MAIN_STREAM, record.getId());
                                    redisTemplate.opsForStream().add(RETRY_STREAM, Map.of("msg", json));
                                    return Mono.empty();
                                });
                    } catch (JsonProcessingException e) {
                        log.error("메시지 파싱 실패: {}", json, e);
                        return Mono.empty();
                    }
                })
                .subscribe();
    }

    private void processRetryStream() {
        List<MapRecord<String, String, String>> records =
                redisTemplate.<String, String>opsForStream()
                        .read(StreamReadOptions.empty().count(100),
                              StreamOffset.fromStart(RETRY_STREAM));

        if (records == null) return;

        Flux.fromIterable(records)
                .concatMap(record -> {
                    String json = record.getValue().get("msg");
                    try {
                        PhoneNumber phone = objectMapper.readValue(json, PhoneNumber.class);
                        return sendSmsMessage(phone)
                                .doOnSuccess(v -> redisTemplate.opsForStream().delete(RETRY_STREAM, record.getId()))
                                .onErrorResume(err -> {
                                    // SMS 전송 실패 시 별도 처리 없음
                                    redisTemplate.opsForStream().delete(RETRY_STREAM, record.getId());
                                    log.error("{}: 전송 실패, 재시도 스트림 이동: {}", RETRY_STREAM, phone.getName());
                                    return Mono.empty();
                                });
                    } catch (JsonProcessingException e) {
                        log.error("메시지 파싱 실패: {}", json, e);
                        return Mono.empty();
                    }
                })
                .subscribe();
    }

    private Mono<Void> sendKakaoMessage(PhoneNumber phone) {
        if (rateLimiterService.tryConsume("kakao_" + kakaoId, "kakao")) {
            String auth = kakaoId + ":" + kakaoPassword;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            return webClient.post()
                    .uri(kakaoMessageUrl)
                    .header("Authorization", "Basic " + encodedAuth)
                    .bodyValue(Map.of("phone", phone.getPhone(), "message", String.format(MESSAGE, phone.getName())))
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(res -> log.info("카카오 메세지 발송 성공: {}", res.getStatusCode()))
                    .doOnError(res -> log.error("카카오 메세지 발송 실패: {}", res.getMessage()))
                    .then();
        } else {
            return Mono.error(new RuntimeException("Kakao Rate Limit 초과: " + phone.getName()));
        }
    }

    private Mono<Void> sendSmsMessage(PhoneNumber phone) {
        if (rateLimiterService.tryConsume("sms_" + smsId, "sms")) {
            String auth = smsId + ":" + smsPassword;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            return webClient.post()
                    .uri(UriComponentsBuilder.fromUriString(smsMessageUrl)
                                 .queryParam("phone", phone.getPhone())
                                 .build()
                                 .toUriString())
                    .header("Authorization", "Basic " + encodedAuth)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("message", String.format(MESSAGE, phone.getName())))
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(res -> log.info("sms 메세지 발송 성공: {}", res.getStatusCode()))
                    .doOnError(res -> log.error("sms 메세지 발송 실패: {}", res.getMessage()))
                    .then();
        } else {
            return Mono.error(new RuntimeException("SMS Rate Limit 초과: " + phone.getName()));
        }
    }
}
