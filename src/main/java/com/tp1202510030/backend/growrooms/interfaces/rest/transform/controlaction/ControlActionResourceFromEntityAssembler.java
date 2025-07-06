package com.tp1202510030.backend.growrooms.interfaces.rest.transform.controlaction;

import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.controlaction.ControlActionResource;

public class ControlActionResourceFromEntityAssembler {
    public static ControlActionResource toResourceFromEntity(ControlAction entity) {
        return new ControlActionResource(
                entity.getId(),
                entity.getActuatorType(),
                entity.getControlActionType(),
                entity.getTriggeringReason(),
                entity.getTriggeringParameterType(),
                entity.getTriggeringMeasurementValue(),
                entity.getTimestamp(),
                entity.getCropPhase().getId()
        );
    }
}
