package com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom;

import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.MeasurementResource;

import java.util.List;

public record GrowRoomResource(
        Long id,
        String name,
        String imageUrl,
        List<MeasurementResource> latestMeasurements
) {
}
