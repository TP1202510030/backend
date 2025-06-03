package com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;

import java.time.Instant;

public record MeasurementResource(
        Long id,
        Parameters parameter,
        Double value,
        Instant timestamp,
        Long cropPhaseId
) {}
