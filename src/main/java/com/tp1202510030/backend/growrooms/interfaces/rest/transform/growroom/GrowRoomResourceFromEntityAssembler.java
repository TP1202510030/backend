package com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.GrowRoomResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.MeasurementResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement.MeasurementResourceFromEntityAssembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrowRoomResourceFromEntityAssembler {
    public static GrowRoomResource toResourceFromEntity(GrowRoom entity) {

        List<MeasurementResource> latestMeasurements =
                (entity.getLatestMeasurements() != null) ?
                        entity.getLatestMeasurements().stream()
                                .map(MeasurementResourceFromEntityAssembler::toResourceFromEntity)
                                .collect(Collectors.toList()) :
                        new ArrayList<>();

        return new GrowRoomResource(
                entity.getId(),
                entity.getGrowRoomName(),
                entity.getImageUrl(),
                latestMeasurements,
                entity.getHasActiveCrop()
        );
    }
}
