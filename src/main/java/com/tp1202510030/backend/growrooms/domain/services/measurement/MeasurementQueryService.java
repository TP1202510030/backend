package com.tp1202510030.backend.growrooms.domain.services.measurement;

import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetAllMeasurementsByCropPhaseIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetMeasurementsForCurrentPhaseByCropIdQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeasurementQueryService {
    Page<Measurement> handle(GetAllMeasurementsByCropPhaseIdQuery query, Pageable pageable);

    Page<Measurement> handle(GetMeasurementsForCurrentPhaseByCropIdQuery query, Pageable pageable);
}