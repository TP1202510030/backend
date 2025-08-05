package com.tp1202510030.backend.growrooms.application.internal.queryservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetAllControlActionsByCropPhaseIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetControlActionsForCurrentPhaseByCropIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.controlaction.ControlActionQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.ControlActionRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropPhaseRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ControlActionQueryServiceImpl implements ControlActionQueryService {
    private final ControlActionRepository controlActionRepository;
    private final CropRepository cropRepository;
    private final CropPhaseRepository cropPhaseRepository;

    public ControlActionQueryServiceImpl(ControlActionRepository controlActionRepository, CropRepository cropRepository, CropPhaseRepository cropPhaseRepository) {
        this.controlActionRepository = controlActionRepository;
        this.cropRepository = cropRepository;
        this.cropPhaseRepository = cropPhaseRepository;
    }

    @Override
    public Page<ControlAction> handle(GetAllControlActionsByCropPhaseIdQuery query, Pageable pageable) {
        cropPhaseRepository.findById(query.cropPhaseId())
                .orElseThrow(() -> new IllegalArgumentException("CropPhase with ID " + query.cropPhaseId() + " not found."));

        return controlActionRepository.findAllByCropPhaseId(query.cropPhaseId(), pageable);
    }

    @Override
    public Page<ControlAction> handle(GetControlActionsForCurrentPhaseByCropIdQuery query, Pageable pageable) {
        Crop crop = cropRepository.findById(query.cropId())
                .orElseThrow(() -> new IllegalArgumentException("Crop with ID " + query.cropId() + " not found"));

        if (crop.getCurrentPhase() == null) {
            throw new IllegalStateException("The crop does not have a current phase.");
        }

        return controlActionRepository.findAllByCropPhaseId(crop.getCurrentPhase().getId(), pageable);
    }
}