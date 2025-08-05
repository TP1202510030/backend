package com.tp1202510030.backend.growrooms.application.internal.queryservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetAllMeasurementsByCropPhaseIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.measurement.GetMeasurementsForCurrentPhaseByCropIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.measurement.MeasurementQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropPhaseRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.MeasurementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MeasurementQueryServiceImpl implements MeasurementQueryService {
    private final MeasurementRepository measurementRepository;
    private final CropRepository cropRepository;
    private final CropPhaseRepository cropPhaseRepository;

    public MeasurementQueryServiceImpl(MeasurementRepository measurementRepository, CropRepository cropRepository, CropPhaseRepository cropPhaseRepository) {
        this.measurementRepository = measurementRepository;
        this.cropRepository = cropRepository;
        this.cropPhaseRepository = cropPhaseRepository;
    }

    @Override
    public Page<Measurement> handle(GetAllMeasurementsByCropPhaseIdQuery query, Pageable pageable) {
        cropPhaseRepository.findById(query.cropPhaseId())
                .orElseThrow(() -> new IllegalArgumentException("CropPhase with ID " + query.cropPhaseId() + " not found."));

        return this.measurementRepository.findAllByCropPhaseId(query.cropPhaseId(), pageable);
    }

    @Override
    public Page<Measurement> handle(GetMeasurementsForCurrentPhaseByCropIdQuery query, Pageable pageable) {
        Crop crop = cropRepository.findById(query.cropId())
                .orElseThrow(() -> new IllegalArgumentException("Crop with ID " + query.cropId() + " not found"));

        if (crop.getCurrentPhase() == null) {
            throw new IllegalStateException("The crop does not have a current phase.");
        }

        return measurementRepository.findAllByCropPhaseId(crop.getCurrentPhase().getId(), pageable);
    }
}