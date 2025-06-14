package com.tp1202510030.backend.growrooms.application.internal.eventhandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tp1202510030.backend.growrooms.domain.model.events.ThresholdsUpdatedEvent;
import com.tp1202510030.backend.growrooms.domain.services.messagebroker.MessageBroker;
import com.tp1202510030.backend.shared.utils.SlugUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ThresholdsPublicationEventHandler {

    private final MessageBroker messageBroker;
    private final ObjectMapper objectMapper;

    public ThresholdsPublicationEventHandler(MessageBroker messageBroker, ObjectMapper objectMapper) {
        this.messageBroker = messageBroker;
        this.objectMapper = objectMapper;
    }

    private static final String THRESHOLD_TOPIC = "threshold";
    private static final String GROW_ROOM_ID_FIELD = "growRoomId";
    private static final String PARAMETER_THRESHOLDS_FIELD = "parameterThresholds";
    private static final String SENSOR_ACTIVATION_FREQUENCY = "sensorActivationFrequency";

    @EventListener
    public void on(ThresholdsUpdatedEvent event) {
        var crop = event.getCrop();
        var phase = event.getPhase();

        var topicName = SlugUtils.toSlug(THRESHOLD_TOPIC);
        var growRoomId = crop.getGrowRoom().getId();
        var sensorActivationFrequency = crop.getSensorActivationFrequency();

        try {
            ObjectNode rootNode = objectMapper.createObjectNode();

            rootNode.put(GROW_ROOM_ID_FIELD, growRoomId);
            rootNode.put(SENSOR_ACTIVATION_FREQUENCY, sensorActivationFrequency.toSeconds());

            if (phase != null) {
                rootNode.set(PARAMETER_THRESHOLDS_FIELD, objectMapper.valueToTree(phase.getThresholds()));
            } else {
                rootNode.putNull(PARAMETER_THRESHOLDS_FIELD);
            }

            String thresholdsJson = objectMapper.writeValueAsString(rootNode);

            messageBroker.publish(topicName, thresholdsJson);

        } catch (Exception e) {
            System.err.println("Failed to serialize thresholds for publishing: " + e.getMessage());
        }
    }
}