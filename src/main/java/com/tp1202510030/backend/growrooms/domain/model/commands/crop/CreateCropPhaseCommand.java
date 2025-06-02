package com.tp1202510030.backend.growrooms.domain.model.commands.crop;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ParameterThresholds;

import java.time.Duration;

public record CreateCropPhaseCommand(
        String name,
        Duration phaseDuration,
        ParameterThresholds parameterThresholds
) {
}
