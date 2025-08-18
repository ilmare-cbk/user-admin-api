package com.cbk.user_admin_api.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Value("${kafka.topics.kakao-message}")
    private String kakaoMessageSendTopic;

    @Value("${kafka.topics.kakao-message-retry}")
    private String kakaoMessageRetryTopic;

    @Value(("${kafka.topics.sms}"))
    private String smsSendTopic;

    @Bean
    public NewTopic kakaoMessageSendTopic() {
        return new NewTopic(kakaoMessageSendTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic kakaoMessageRetryTopic() {
        return new NewTopic(kakaoMessageRetryTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic smsSendTopic() {
        return new NewTopic(smsSendTopic, 1, (short) 1);
    }
}
