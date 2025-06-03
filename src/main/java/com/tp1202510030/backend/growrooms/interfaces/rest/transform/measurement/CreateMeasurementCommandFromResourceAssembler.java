package com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement;

import com.tp1202510030.backend.growrooms.domain.model.commands.measurement.CreateMeasurementCommand;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.CreateMeasurementResource;

public class CreateMeasurementCommandFromResourceAssembler {

    public static CreateMeasurementCommand toCommandFromResource(CreateMeasurementResource resource) {
        return new CreateMeasurementCommand(
                resource.parameter(),
                resource.value(),
                resource.timestamp(),
                resource.cropPhaseId()
        );
    }
}
