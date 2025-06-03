package com.tp1202510030.backend.growrooms.application.internal.queryservices;

import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import com.tp1202510030.backend.growrooms.domain.model.queries.cropPhase.GetCropPhaseByIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.cropphase.CropPhaseQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropPhaseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CropPhaseQueryServiceImpl implements CropPhaseQueryService {
    private final CropPhaseRepository cropPhaseRepository;

    public CropPhaseQueryServiceImpl(CropPhaseRepository cropPhaseRepository) {
        this.cropPhaseRepository = cropPhaseRepository;
    }

    @Override
    public Optional<CropPhase> handle(GetCropPhaseByIdQuery query) {
        return this.cropPhaseRepository.findById(query.id());
    }
}
