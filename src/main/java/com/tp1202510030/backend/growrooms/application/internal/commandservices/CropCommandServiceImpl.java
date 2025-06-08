package com.tp1202510030.backend.growrooms.application.internal.commandservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.AdvanceCropPhaseCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.crop.CreateCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomByIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropCommandService;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CropCommandServiceImpl implements CropCommandService {
    private final CropRepository cropRepository;
    private final GrowRoomQueryService growRoomQueryService;

    public CropCommandServiceImpl(CropRepository cropRepository, GrowRoomQueryService growRoomQueryService) {
        this.cropRepository = cropRepository;
        this.growRoomQueryService = growRoomQueryService;
    }

    @Override
    public Long handle(CreateCropCommand command) {
        var growRoomOpt = growRoomQueryService.handle(new GetGrowRoomByIdQuery(command.growRoomId()));
        if (growRoomOpt.isEmpty()) {
            throw new IllegalArgumentException("Grow room with ID " + command.growRoomId() + " not found");
        }
        var growRoom = growRoomOpt.get();

        List<CropPhase> cropPhases = command.phases().stream()
                .map(phaseCmd -> new CropPhase(
                        phaseCmd.name(),
                        phaseCmd.phaseDuration(),
                        phaseCmd.parameterThresholds()
                ))
                .toList();

        boolean hasActiveCrop = cropRepository.findFirstByGrowRoomIdAndEndDateIsNull(growRoom.getId()).isPresent();
        if (hasActiveCrop) {
            throw new IllegalStateException("Grow room with ID " + command.growRoomId() + " already has an active crop");
        }

        var crop = new Crop(
                command.startDate(),
                command.endDate(),
                command.sensorActivationFrequency(),
                growRoom,
                cropPhases
        );

        if (!cropPhases.isEmpty()) {
            crop.updateCurrentPhase(cropPhases.getFirst());
            cropPhases.forEach(phase -> phase.setCrop(crop));
        }

        cropRepository.save(crop);
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

        int currentIndex = -1;
        if (currentPhase != null) {
            currentIndex = phases.indexOf(currentPhase);
            if (currentIndex == -1) {
                throw new IllegalStateException("Current phase is not in the list of phases");
            }
        }

        if (currentIndex >= phases.size() - 1) {
            if (crop.getEndDate() == null) {
                crop.setEndDate(new Date());
            }
            crop.updateCurrentPhase(currentPhase);
            cropRepository.save(crop);
            return;
        }

        CropPhase nextPhase = phases.get(currentIndex + 1);
        crop.updateCurrentPhase(nextPhase);

        cropRepository.save(crop);
    }
}
