package com.tp1202510030.backend.growrooms.domain.services.measurement;

import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetAllMeasurementsByCropPhaseId;

import java.util.List;

public interface MeasurementQueryService {
    List<Measurement> handle(GetAllMeasurementsByCropPhaseId query);
}
