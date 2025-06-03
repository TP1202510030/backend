package com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowPhaseName;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ParameterThresholds;

import java.time.Duration;

public record CropPhaseResource(
        Long id,
        GrowPhaseName name,
        Duration duration,
        ParameterThresholds thresholds
) {}