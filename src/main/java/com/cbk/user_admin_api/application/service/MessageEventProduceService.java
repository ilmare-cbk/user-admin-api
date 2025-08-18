package com.cbk.user_admin_api.application.service;

import java.util.List;

public interface MessageEventProduceService {
    void produceEvent(String topic, String event);

    void produceEvents(String topic, List<String> events);
}
