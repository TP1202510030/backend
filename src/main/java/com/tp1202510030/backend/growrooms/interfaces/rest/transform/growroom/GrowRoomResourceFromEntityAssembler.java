package com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.GrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement.MeasurementResourceFromEntityAssembler;

public class GrowRoomResourceFromEntityAssembler {
    public static GrowRoomResource toResourceFromEntity(GrowRoom entity) {
        return new GrowRoomResource(
                entity.getId(),
                entity.getGrowRoomName(),
                entity.getImageUrl(),
                entity.getCompany(),
                entity.getLatestMeasurement() != null ?
                        MeasurementResourceFromEntityAssembler.toResourceFromEntity(entity.getLatestMeasurement()) : null
        );
    }
}
