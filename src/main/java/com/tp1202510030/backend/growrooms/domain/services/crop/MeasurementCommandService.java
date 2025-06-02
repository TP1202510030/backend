package com.tp1202510030.backend.growrooms.domain.services.crop;

import com.tp1202510030.backend.growrooms.domain.model.commands.crop.CreateMeasurementCommand;

public interface MeasurementCommandService {
    void handle(CreateMeasurementCommand command);
}
