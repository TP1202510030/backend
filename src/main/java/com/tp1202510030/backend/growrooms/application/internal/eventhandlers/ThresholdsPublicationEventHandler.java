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

    @EventListener
    public void on(ThresholdsUpdatedEvent event) {
        var crop = event.getCrop();
        var phase = event.getPhase(); // La fase actual, o null si el cultivo terminó

        var growRoom = crop.getGrowRoom();
        var company = growRoom.getCompany();

        var topicName = SlugUtils.toSlug(company.getCompanyName(), growRoom.getGrowRoomName(), "threshold");

        try {
            // Crear el objeto contenedor principal
            ObjectNode rootNode = objectMapper.createObjectNode();

            if (phase != null) {
                // Si hay una fase, serializar sus thresholds y ponerlos dentro del contenedor
                rootNode.set("parameterThresholds", objectMapper.valueToTree(phase.getThresholds()));
            } else {
                // Si no hay fase (cultivo finalizado), poner null
                rootNode.putNull("parameterThresholds");
            }

            // Convertir el objeto principal a un string JSON
            String thresholdsJson = objectMapper.writeValueAsString(rootNode);

            //System.out.println("Publishing to topic: " + topicName);
            //System.out.println("Payload: " + thresholdsJson);

            messageBroker.publish(topicName, thresholdsJson);

        } catch (Exception e) {
            // Manejar la excepción de serialización
            System.err.println("Failed to serialize thresholds for publishing: " + e.getMessage());
        }
    }
}