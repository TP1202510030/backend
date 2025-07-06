package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.growrooms.domain.model.commands.controlaction.AddControlActionsToCurrentPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import com.tp1202510030.backend.growrooms.domain.services.controlaction.ControlActionCommandService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.ControlActionRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import org.springframework.stereotype.Service;

@Service
public class ControlActionCommandServiceImpl implements ControlActionCommandService {
    private final ControlActionRepository controlActionRepository;
    private final CropRepository cropRepository;

    public ControlActionCommandServiceImpl(ControlActionRepository controlActionRepository, CropRepository cropRepository) {
        this.controlActionRepository = controlActionRepository;
        this.cropRepository = cropRepository;
    }

    @Override
    public void handle(AddControlActionsToCurrentPhaseCommand command) {
        var currentPhase = cropRepository.findActivePhaseByCropIdOrThrow(command.cropId());

        var controlActions = command.controlActions().stream()
                .map(actionDto -> new ControlAction(
                        actionDto.actuatorType(),
                        actionDto.controlActionType(),
                        actionDto.triggeringReason(),
                        actionDto.triggeringParameterType(),
                        actionDto.triggeringMeasurementValue(),
                        currentPhase
                ))
                .toList();

        try {
            controlActionRepository.saveAll(controlActions);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving control actions: " + e.getMessage(), e);
        }
    }
}
