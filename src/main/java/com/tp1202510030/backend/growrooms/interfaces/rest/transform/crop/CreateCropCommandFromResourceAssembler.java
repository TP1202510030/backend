package com.tp1202510030.backend.growrooms.interfaces.rest.transform.crop;

import com.tp1202510030.backend.growrooms.domain.model.commands.crop.CreateCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.cropphase.CreateCropPhaseCommand;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop.CreateCropResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.cropphase.CreateCropPhaseCommandFromResourceAssembler;

import java.util.List;

public class CreateCropCommandFromResourceAssembler {
    public static CreateCropCommand toCommandFromResource(CreateCropResource resource, Long growRoomId) {
        List<CreateCropPhaseCommand> phases = resource.phases().stream()
                .map(CreateCropPhaseCommandFromResourceAssembler::toCommandFromResource)
                .toList();

        return new CreateCropCommand(
                resource.startDate(),
                resource.endDate(),
                resource.sensorActivationFrequency(),
                growRoomId,
                phases
        );
    }
}
