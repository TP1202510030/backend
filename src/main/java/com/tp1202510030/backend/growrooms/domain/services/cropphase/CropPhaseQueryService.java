package com.tp1202510030.backend.growrooms.domain.services.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import com.tp1202510030.backend.growrooms.domain.model.queries.cropPhase.GetCropPhaseByIdQuery;

import java.util.Optional;

public interface CropPhaseQueryService {
    Optional<CropPhase> handle(GetCropPhaseByIdQuery query);
}
