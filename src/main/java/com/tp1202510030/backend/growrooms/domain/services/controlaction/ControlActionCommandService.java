package com.tp1202510030.backend.growrooms.domain.services.controlaction;

import com.tp1202510030.backend.growrooms.domain.model.commands.controlaction.AddControlActionsToCurrentPhaseCommand;

public interface ControlActionCommandService {
    void handle(AddControlActionsToCurrentPhaseCommand command);
}
