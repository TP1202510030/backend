package com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement;

import com.tp1202510030.backend.growrooms.domain.model.commands.measurement.AddMeasurementsToCurrentPhaseCommand;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.CreateMeasurementResource;

import java.util.List;

public class AddMeasurementsToCurrentPhaseCommandFromResourceAssembler {

    public static AddMeasurementsToCurrentPhaseCommand toCommand(Long cropId, List<CreateMeasurementResource> measurementResources) {
        List<AddMeasurementsToCurrentPhaseCommand.MeasurementEntry> entries = measurementResources.stream()
                .map(resource -> new AddMeasurementsToCurrentPhaseCommand.MeasurementEntry(
                        resource.parameter(),
                        resource.value(),
                        resource.timestamp()
                ))
                .toList();

        return new AddMeasurementsToCurrentPhaseCommand(cropId, entries);
    }
}
