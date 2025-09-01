package com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ParameterThresholds;

import java.time.Duration;

public record CreateCropPhaseResource(
        String name,
        Duration phaseDuration,
        ParameterThresholds parameterThresholds
) {
    public CreateCropPhaseResource {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null or negative");
        }
        if (phaseDuration == null || phaseDuration.isNegative() || phaseDuration.isZero()) {
            throw new IllegalArgumentException("phaseDuration cannot be null, negative, or zero");
        }
        if (parameterThresholds == null) {
            throw new IllegalArgumentException("parameterThresholds cannot be null");
        }
        if (parameterThresholds.hasNullValues()) {
            throw new IllegalArgumentException("All parameter threshold values must be provided and cannot be null");
        }
    }
}
