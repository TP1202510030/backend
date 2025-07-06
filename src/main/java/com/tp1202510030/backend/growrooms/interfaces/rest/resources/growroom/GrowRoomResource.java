package com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ActuatorType;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.MeasurementResource;

import java.util.List;
import java.util.Map;

public record GrowRoomResource(
        Long id,
        String name,
        String imageUrl,
        boolean hasActiveCrop,
        List<MeasurementResource> latestMeasurements,
        Map<ActuatorType, String> actuatorStates,
        Long activeCropId
) {
}
