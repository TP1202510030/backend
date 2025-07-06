package com.tp1202510030.backend.growrooms.interfaces.rest.transform.controlaction;

import com.tp1202510030.backend.growrooms.domain.model.commands.controlaction.AddControlActionsToCurrentPhaseCommand;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.controlaction.CreateControlActionResource;

import java.util.List;

public class AddControlActionsToCurrentPhaseCommandFromResourceAssembler {
    public static AddControlActionsToCurrentPhaseCommand toCommandFromResourceAssembler(Long cropId, List<CreateControlActionResource> controlActionResources) {
        List<AddControlActionsToCurrentPhaseCommand.ControlActionEntry> entries = controlActionResources.stream()
                .map(resource -> new AddControlActionsToCurrentPhaseCommand.ControlActionEntry(
                        resource.actuatorType(),
                        resource.controlActionType(),
                        resource.triggeringReason(),
                        resource.triggeringParameterType(),
                        resource.triggeringMeasurementValue(),
                        resource.triggeringMeasurementValue()
                ))
                .toList();

        return new AddControlActionsToCurrentPhaseCommand(cropId, entries);
    }
}