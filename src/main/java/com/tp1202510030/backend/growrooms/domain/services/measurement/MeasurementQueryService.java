package com.tp1202510030.backend.growrooms.domain.services.measurement;

import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetAllMeasurementsByCropPhaseIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetMeasurementsForCurrentPhaseByCropIdQuery;

import java.util.List;

public interface MeasurementQueryService {
    List<Measurement> handle(GetAllMeasurementsByCropPhaseIdQuery query);

    List<Measurement> handle(GetMeasurementsForCurrentPhaseByCropIdQuery query);
}
