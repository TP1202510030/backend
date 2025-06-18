package com.tp1202510030.backend.growrooms.application.internal.queryservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetAllControlActionsByCropPhaseIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetControlActionsForCurrentPhaseByCropIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.controlaction.ControlActionQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.ControlActionRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ControlActionQueryServiceImpl implements ControlActionQueryService {
    private final ControlActionRepository controlActionRepository;
    private final CropRepository cropRepository;

    public ControlActionQueryServiceImpl(ControlActionRepository controlActionRepository, CropRepository cropRepository) {
        this.controlActionRepository = controlActionRepository;
        this.cropRepository = cropRepository;
    }

    @Override
    public List<ControlAction> handle(GetAllControlActionsByCropPhaseIdQuery query) {
        return controlActionRepository.findAllByCropPhaseId(query.cropPhaseId());
    }

    @Override
    public List<ControlAction> handle(GetControlActionsForCurrentPhaseByCropIdQuery query) {
        Crop crop = cropRepository.findById(query.cropId())
                .orElseThrow(() -> new IllegalArgumentException("Crop with ID " + query.cropId() + " not found"));

        if (crop.getCurrentPhase() == null) {
            throw new IllegalStateException("The crop does not have a current phase.");
        }

        return controlActionRepository.findAllByCropPhaseId(crop.getCurrentPhase().getId());
    }
}
