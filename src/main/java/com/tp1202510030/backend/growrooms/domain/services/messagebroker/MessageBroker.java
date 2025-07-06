package com.tp1202510030.backend.growrooms.domain.services.messagebroker;

public interface MessageBroker {
    void publish(String topic, String message);
}
