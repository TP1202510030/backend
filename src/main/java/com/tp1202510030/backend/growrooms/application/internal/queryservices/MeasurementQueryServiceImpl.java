package com.tp1202510030.backend.growrooms.application.internal.queryservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetAllMeasurementsByCropPhaseIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetMeasurementsForCurrentPhaseByCropIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementQueryServiceImpl implements MeasurementQueryService {
    private final MeasurementRepository measurementRepository;
    private final CropRepository cropRepository;

    public MeasurementQueryServiceImpl(MeasurementRepository measurementRepository, CropRepository cropRepository) {
        this.measurementRepository = measurementRepository;
        this.cropRepository = cropRepository;
    }

    @Override
    public List<Measurement> handle(GetAllMeasurementsByCropPhaseIdQuery query) {
        return this.measurementRepository.findAllByCropPhaseId(query.cropPhaseId());
    }

    @Override
    public List<Measurement> handle(GetMeasurementsForCurrentPhaseByCropIdQuery query) {
        Crop crop = cropRepository.findById(query.cropId())
                .orElseThrow(() -> new IllegalArgumentException("Crop with ID " + query.cropId() + " not found"));

        if (crop.getCurrentPhase() == null) {
            throw new IllegalStateException("The crop does not have a current phase.");
        }

        return measurementRepository.findAllByCropPhaseId(crop.getCurrentPhase().getId());
    }
}
