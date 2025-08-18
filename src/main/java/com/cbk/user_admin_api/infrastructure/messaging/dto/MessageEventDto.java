package com.cbk.user_admin_api.infrastructure.messaging.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class MessageEventDto {
    private String phone;
    private String name;

    public static MessageEventDto parseMessage(ObjectMapper mapper, String message) {
        MessageEventDto event;
        try {
            event = mapper.readValue(message, MessageEventDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return event;
    }
}
