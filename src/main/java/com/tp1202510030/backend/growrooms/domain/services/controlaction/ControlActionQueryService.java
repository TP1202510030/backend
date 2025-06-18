package com.tp1202510030.backend.growrooms.domain.services.controlaction;

import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetAllControlActionsByCropPhaseIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.controlaction.GetControlActionsForCurrentPhaseByCropIdQuery;

import java.util.List;

public interface ControlActionQueryService {
    List<ControlAction> handle(GetAllControlActionsByCropPhaseIdQuery query);

    List<ControlAction> handle(GetControlActionsForCurrentPhaseByCropIdQuery query);
}
