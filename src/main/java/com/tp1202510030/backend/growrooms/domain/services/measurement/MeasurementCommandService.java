package com.tp1202510030.backend.growrooms.domain.services.measurement;

import com.tp1202510030.backend.growrooms.domain.model.commands.measurement.AddMeasurementsToCurrentPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.measurement.CreateMeasurementCommand;

public interface MeasurementCommandService {
    Long handle(CreateMeasurementCommand command);
    void handle(AddMeasurementsToCurrentPhaseCommand command);
}
