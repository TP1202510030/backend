package com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ParameterThresholds;

import java.time.Duration;

public record CreateCropPhaseResource(
        String name,
        Duration phaseDuration,
        ParameterThresholds parameterThresholds
) {}
