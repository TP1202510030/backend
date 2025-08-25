package com.tp1202510030.backend.growrooms.interfaces.rest.resources.controlaction;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ActuatorType;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ControlActionType;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;

public record ControlActionResource(
        Long id,
        ActuatorType actuatorType,
        ControlActionType controlActionType,
        String triggeringReason,
        Parameters triggeringParameterType,
        Double triggeringMeasurementValue,
        String timestamp,
        Long cropPhaseId
) {
}
