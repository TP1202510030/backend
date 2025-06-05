package com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;

public record CreateMeasurementResource(
        Parameters parameter,
        Double value
) {
}
