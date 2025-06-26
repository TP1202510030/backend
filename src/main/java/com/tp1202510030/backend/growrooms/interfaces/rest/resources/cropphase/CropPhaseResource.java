package com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ParameterThresholds;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.MeasurementResource;

import java.time.Duration;
import java.util.List;

public record CropPhaseResource(
        Long id,
        String name,
        Duration duration,
        ParameterThresholds thresholds,
        List<MeasurementResource> measurements
) {
}