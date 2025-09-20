package com.tp1202510030.backend.growrooms.application.internal.services;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropDeletionService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.ControlActionRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropPhaseRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CropDeletionServiceImpl implements CropDeletionService {

    private final CropRepository cropRepository;
    private final CropPhaseRepository cropPhaseRepository;
    private final MeasurementRepository measurementRepository;
    private final ControlActionRepository controlActionRepository;

    public CropDeletionServiceImpl(CropRepository cropRepository, CropPhaseRepository cropPhaseRepository, MeasurementRepository measurementRepository, ControlActionRepository controlActionRepository) {
        this.cropRepository = cropRepository;
        this.cropPhaseRepository = cropPhaseRepository;
        this.measurementRepository = measurementRepository;
        this.controlActionRepository = controlActionRepository;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteCropAndAssociations(Crop crop) {
        for (CropPhase phase : crop.getPhases()) {
            measurementRepository.deleteAll(phase.getMeasurements());
            controlActionRepository.deleteAll(phase.getControlActions());
        }
        cropPhaseRepository.deleteAll(crop.getPhases());
        cropRepository.delete(crop);
    }
}
