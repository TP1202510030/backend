package com.tp1202510030.backend.growrooms.domain.services.measurement;

import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetMeasurementByIdQuery;

import java.util.Optional;

public interface MeasurementQueryService {
    Optional<Measurement> handle(GetMeasurementByIdQuery query);
}
