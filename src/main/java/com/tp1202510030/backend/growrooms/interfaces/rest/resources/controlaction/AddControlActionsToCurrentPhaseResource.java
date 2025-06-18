package com.tp1202510030.backend.growrooms.interfaces.rest.resources.controlaction;

import java.util.List;

public record AddControlActionsToCurrentPhaseResource(
        List<CreateControlActionResource> controlActions
) {
}
