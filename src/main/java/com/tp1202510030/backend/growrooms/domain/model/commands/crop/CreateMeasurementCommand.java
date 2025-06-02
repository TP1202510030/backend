package com.tp1202510030.backend.growrooms.domain.model.commands.crop;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;

import java.time.Instant;

public record CreateMeasurementCommand(
        Long cropPhaseId,
        Parameters parameter,
        Double value,
        Instant timestamp
) {}
