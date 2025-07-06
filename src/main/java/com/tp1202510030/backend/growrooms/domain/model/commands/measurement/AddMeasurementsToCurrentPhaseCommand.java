package com.tp1202510030.backend.growrooms.domain.model.commands.measurement;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;

import java.util.List;

public record AddMeasurementsToCurrentPhaseCommand(Long cropId, List<MeasurementEntry> measurements) {
    public record MeasurementEntry(
            Parameters parameter,
            Double value
    ) {
    }
}
