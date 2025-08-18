package com.cbk.user_admin_api.infrastructure.messaging;

/**
 * event 컴슘 후 외부 API 통해서 message 발송하는 서비스
 */
public interface MessageSendService {
    boolean isSupported(MessageHandlerKey key);
    void send(String message);
}
