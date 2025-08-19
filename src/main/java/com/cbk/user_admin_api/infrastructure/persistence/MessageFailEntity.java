package com.cbk.user_admin_api.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_fail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageFailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    private String message;

    public static MessageFailEntity create(MessageType type, String message) {
        MessageFailEntity entity = new MessageFailEntity();
        entity.type = type;
        entity.message = message;
        return entity;
    }
}
