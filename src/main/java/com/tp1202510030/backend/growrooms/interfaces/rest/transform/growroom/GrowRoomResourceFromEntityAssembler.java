package com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.GrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement.MeasurementResourceFromEntityAssembler;

import java.util.stream.Collectors;

public class GrowRoomResourceFromEntityAssembler {
    public static GrowRoomResource toResourceFromEntity(GrowRoom entity) {
        var latestMeasurements = entity.getLatestMeasurements().stream()
                .map(MeasurementResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        var actuatorStates = entity.getActuatorStates().entrySet().stream()
                .collect(Collectors.toMap(
                        java.util.Map.Entry::getKey,
                        entry -> entry.getValue().name()
                ));


        return new GrowRoomResource(
                entity.getId(),
                entity.getGrowRoomName(),
                entity.getImageUrl(),
                entity.getHasActiveCrop(),
                latestMeasurements,
                actuatorStates
        );
    }
}
