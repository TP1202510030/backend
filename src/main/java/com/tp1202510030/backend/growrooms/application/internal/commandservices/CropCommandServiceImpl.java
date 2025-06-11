package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.AdvanceCropPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.CreateCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.ActivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.DeactivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import com.tp1202510030.backend.growrooms.domain.model.events.ThresholdsUpdatedEvent; // IMPORTANTE: Cambiar el import
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomByIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropCommandService;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomCommandService;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CropCommandServiceImpl implements CropCommandService {
    private final CropRepository cropRepository;
    private final GrowRoomQueryService growRoomQueryService;
    private final GrowRoomCommandService growRoomCommandService;
    private final ApplicationEventPublisher eventPublisher;

    public CropCommandServiceImpl(CropRepository cropRepository, GrowRoomQueryService growRoomQueryService, GrowRoomCommandService growRoomCommandService, ApplicationEventPublisher eventPublisher) {
        this.cropRepository = cropRepository;
        this.growRoomQueryService = growRoomQueryService;
        this.growRoomCommandService = growRoomCommandService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Long handle(CreateCropCommand command) {
        var growRoomOpt = growRoomQueryService.handle(new GetGrowRoomByIdQuery(command.growRoomId()));
        if (growRoomOpt.isEmpty()) {
            throw new IllegalArgumentException("Grow room with ID " + command.growRoomId() + " not found");
        }
        var growRoom = growRoomOpt.get();

        if (growRoom.getHasActiveCrop()) {
            throw new IllegalStateException("Grow room with ID " + command.growRoomId() + " already has an active crop");
        }

        List<CropPhase> cropPhases = command.phases().stream()
                .map(phaseCmd -> new CropPhase(
                        phaseCmd.name(),
                        phaseCmd.phaseDuration(),
                        phaseCmd.parameterThresholds()
                ))
                .toList();

        var crop = new Crop(
                command.startDate(),
                command.endDate(),
                command.sensorActivationFrequency(),
                growRoom,
                cropPhases
        );

        CropPhase firstPhase = null;
        if (!cropPhases.isEmpty()) {
            firstPhase = cropPhases.getFirst();
            crop.updateCurrentPhase(firstPhase);
            cropPhases.forEach(phase -> phase.setCrop(crop));
        }

        cropRepository.save(crop);
        growRoomCommandService.handle(new ActivateGrowRoomCropCommand(growRoom.getId()));

        // **NUEVA LÓGICA:** Publicar solo la primera fase
        if (firstPhase != null) {
            eventPublisher.publishEvent(new ThresholdsUpdatedEvent(this, crop, firstPhase));
        }

        return crop.getId();
    }

    @Override
    public void handle(AdvanceCropPhaseCommand command) {
        var cropOpt = cropRepository.findById(command.cropId());
        if (cropOpt.isEmpty()) {
            throw new IllegalArgumentException("Crop with ID " + command.cropId() + " not found");
        }

        var crop = cropOpt.get();
        List<CropPhase> phases = crop.getPhases();

        if (phases == null || phases.isEmpty()) {
            throw new IllegalStateException("Crop with ID " + command.cropId() + " has no phases");
        }

        var currentPhase = crop.getCurrentPhase();
        int currentIndex = (currentPhase != null) ? phases.indexOf(currentPhase) : -1;

        if (currentIndex >= phases.size() - 1) {
            // **NUEVA LÓGICA:** El cultivo ha finalizado
            if (crop.getEndDate() == null) {
                crop.setEndDate(new Date());
            }
            crop.updateCurrentPhase(null); // La fase actual ahora es nula
            cropRepository.save(crop);

            var growRoom = crop.getGrowRoom();
            if (growRoom.getHasActiveCrop()) {
                growRoomCommandService.handle(new DeactivateGrowRoomCropCommand(growRoom.getId()));
            }

            // Publicar evento de finalización (con fase nula)
            eventPublisher.publishEvent(new ThresholdsUpdatedEvent(this, crop, null));

        } else {
            // **NUEVA LÓGICA:** Avanzar a la siguiente fase
            CropPhase nextPhase = phases.get(currentIndex + 1);
            crop.updateCurrentPhase(nextPhase);
            cropRepository.save(crop);

            // Publicar evento con la nueva fase
            eventPublisher.publishEvent(new ThresholdsUpdatedEvent(this, crop, nextPhase));
        }
    }
}