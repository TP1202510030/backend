package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.AdvanceCropPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.CreateCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.DeleteCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.FinishCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import com.tp1202510030.backend.growrooms.domain.model.events.CropCreatedEvent;
import com.tp1202510030.backend.growrooms.domain.model.events.CropFinishedEvent;
import com.tp1202510030.backend.growrooms.domain.model.events.ThresholdsUpdatedEvent;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropCommandService;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropDeletionService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.GrowRoomRepository;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CropCommandServiceImpl implements CropCommandService {
    private final CropRepository cropRepository;
    private final GrowRoomRepository growRoomRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CropDeletionService cropDeletionService;


    public CropCommandServiceImpl(
            CropRepository cropRepository,
            GrowRoomRepository growRoomRepository,
            ApplicationEventPublisher eventPublisher,
            CropDeletionService cropDeletionService
    ) {
        this.cropRepository = cropRepository;
        this.growRoomRepository = growRoomRepository;
        this.eventPublisher = eventPublisher;
        this.cropDeletionService = cropDeletionService;
    }

    @Override
    @Transactional
    public Long handle(CreateCropCommand command) {
        command.phases().forEach(phaseCommand -> {
            if (Objects.isNull(phaseCommand.parameterThresholds())) {
                throw new IllegalArgumentException("Parameter thresholds cannot be null.");
            }
        });

        var growRoom = growRoomRepository.findById(command.growRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Grow room with ID " + command.growRoomId() + " not found"));

        if (growRoom.getHasActiveCrop()) {
            throw new IllegalStateException("Grow room with ID " + command.growRoomId() + " already has an active crop");
        }

        List<CropPhase> cropPhases = command.phases().stream()
                .map(phaseCommand -> new CropPhase(
                        phaseCommand.name(),
                        phaseCommand.phaseDuration(),
                        phaseCommand.parameterThresholds()
                ))
                .toList();

        var crop = new Crop(
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
        eventPublisher.publishEvent(new CropCreatedEvent(this, growRoom.getId()));

        if (firstPhase != null) {
            eventPublisher.publishEvent(new ThresholdsUpdatedEvent(this, crop, firstPhase));
        }

        return crop.getId();
    }

    @Override
    @Transactional
    public void handle(AdvanceCropPhaseCommand command) {
        var crop = cropRepository.findById(command.cropId())
                .orElseThrow(() -> new IllegalArgumentException("Crop with ID " + command.cropId() + " not found"));

        List<CropPhase> phases = crop.getPhases();
        if (phases == null || phases.isEmpty()) {
            throw new IllegalStateException("Crop with ID " + command.cropId() + " has no phases");
        }

        var currentPhase = crop.getCurrentPhase();
        int currentIndex = (currentPhase != null) ? phases.indexOf(currentPhase) : -1;

        if (currentIndex >= phases.size() - 1) {
            throw new IllegalStateException("Cannot advance from the last phase. Use the 'finish' endpoint instead.");
        }

        CropPhase nextPhase = phases.get(currentIndex + 1);
        crop.updateCurrentPhase(nextPhase);
        cropRepository.save(crop);

        eventPublisher.publishEvent(new ThresholdsUpdatedEvent(this, crop, nextPhase));
    }


    @Override
    public void handle(FinishCropCommand command) {
        var crop = cropRepository.findById(command.cropId())
                .orElseThrow(() -> new IllegalArgumentException("Crop with ID " + command.cropId() + " not found"));

        List<CropPhase> phases = crop.getPhases();
        var currentPhase = crop.getCurrentPhase();
        int currentIndex = (currentPhase != null) ? phases.indexOf(currentPhase) : -1;

        if (currentIndex < phases.size() - 1) {
            throw new IllegalStateException("Crop cannot be finished. It is not on its last phase yet.");
        }

        if (crop.getEndDate() == null) {
            crop.setEndDate(new Date());
        }
        crop.updateCurrentPhase(null);

        crop.setTotalProduction(command.totalProduction());

        cropRepository.save(crop);

        var growRoom = crop.getGrowRoom();
        eventPublisher.publishEvent(new CropFinishedEvent(this, growRoom.getId()));

        eventPublisher.publishEvent(new ThresholdsUpdatedEvent(this, crop, null));
    }

    @Override
    @Transactional
    public void handle(DeleteCropCommand command) {
        Crop cropToDelete = cropRepository.findById(command.cropId())
                .orElseThrow(() -> new ResourceNotFoundException("Crop", "ID", command.cropId().toString()));

        GrowRoom growRoom = cropToDelete.getGrowRoom();

        Optional<Crop> activeCropOpt = cropRepository.findFirstByGrowRoomIdAndEndDateIsNull(growRoom.getId());

        if (activeCropOpt.isPresent() && activeCropOpt.get().getId().equals(cropToDelete.getId())) {
            throw new IllegalStateException("Cannot delete a crop that is currently active in its grow room.");
        }

        cropDeletionService.deleteCropAndAssociations(cropToDelete);
    }
}