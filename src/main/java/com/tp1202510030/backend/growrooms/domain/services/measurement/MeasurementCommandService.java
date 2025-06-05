package com.tp1202510030.backend.growrooms.domain.services.measurement;

import com.tp1202510030.backend.growrooms.domain.model.commands.measurement.AddMeasurementsToCurrentPhaseCommand;

public interface MeasurementCommandService {
    void handle(AddMeasurementsToCurrentPhaseCommand command);
}
