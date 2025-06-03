package com.tp1202510030.backend.growrooms.domain.model.commands.measurement;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;

import java.time.Instant;

public record CreateMeasurementCommand(
        Parameters parameter,
        Double value,
        Instant timestamp,
        Long cropPhaseId
) {}
