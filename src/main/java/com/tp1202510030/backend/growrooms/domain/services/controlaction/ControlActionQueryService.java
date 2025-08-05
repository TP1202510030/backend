package com.tp1202510030.backend.growrooms.domain.services.controlaction;

import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetAllControlActionsByCropPhaseIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetControlActionsForCurrentPhaseByCropIdQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ControlActionQueryService {
    Page<ControlAction> handle(GetAllControlActionsByCropPhaseIdQuery query, Pageable pageable);

    Page<ControlAction> handle(GetControlActionsForCurrentPhaseByCropIdQuery query, Pageable pageable);
}