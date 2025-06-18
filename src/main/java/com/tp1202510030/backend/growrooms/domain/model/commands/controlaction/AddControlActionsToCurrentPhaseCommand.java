package com.tp1202510030.backend.growrooms.domain.model.commands.controlaction;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ActuatorType;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ControlActionType;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;

import java.util.List;

public record AddControlActionsToCurrentPhaseCommand(Long cropId,
                                                     List<ControlActionEntry> controlActions) {
    public record ControlActionEntry(
            ActuatorType actuatorType,
            ControlActionType controlActionType,
            String triggeringReason,
            Parameters triggeringParameterType,
            Double triggeringMeasurementValue,
            Double thresholdValue
    ) {
    }
}
