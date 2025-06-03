package com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement;

import java.util.List;

public record AddMeasurementsToCurrentPhaseResource(
        List<CreateMeasurementResource> measurements
) {
}
