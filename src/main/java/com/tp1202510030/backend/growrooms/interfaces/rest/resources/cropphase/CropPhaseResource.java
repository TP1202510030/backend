package com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ParameterThresholds;

import java.time.Duration;

public record CropPhaseResource(
        Long id,
        String name,
        Duration duration,
        ParameterThresholds thresholds
) {
    public CropPhaseResource {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id cannot be null or negative");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (duration == null || duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("duration cannot be null, negative, or zero");
        }
        if (thresholds == null) {
            throw new IllegalArgumentException("thresholds cannot be null");
        }
    }
}