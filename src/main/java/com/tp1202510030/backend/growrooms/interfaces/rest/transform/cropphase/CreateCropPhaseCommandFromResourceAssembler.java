package com.tp1202510030.backend.growrooms.interfaces.rest.transform.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.commands.cropphase.CreateCropPhaseCommand;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase.CreateCropPhaseResource;

public class CreateCropPhaseCommandFromResourceAssembler {
    public static CreateCropPhaseCommand toCommandFromResource(CreateCropPhaseResource resource) {
        return new CreateCropPhaseCommand(
                resource.name(),
                resource.phaseDuration(),
                resource.parameterThresholds()
        );
    }
}
