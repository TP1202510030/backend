package com.tp1202510030.backend.growrooms.infrastructure.messagebrokers;

import com.tp1202510030.backend.growrooms.domain.services.messagebroker.MessageBroker;
import com.tp1202510030.backend.shared.infrastructure.config.MqttConfig;
import org.springframework.stereotype.Service;

@Service
public class MqttMessageBroker implements MessageBroker {

    private final MqttConfig.MqttGateway mqttGateway;

    public MqttMessageBroker(MqttConfig.MqttGateway mqttGateway) {
        this.mqttGateway = mqttGateway;
    }

    @Override
    public void publish(String topic, String message) {
        mqttGateway.sendToMqtt(message, topic);
    }
}