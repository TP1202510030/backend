package com.tp1202510030.backend.growrooms.application.internal.queryservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.entities.ControlAction;
import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomsByCompanyIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ActuatorType;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.ControlActionType;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.ControlActionRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.GrowRoomRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GrowRoomQueryServiceImpl implements GrowRoomQueryService {
    private final GrowRoomRepository growRoomRepository;
    private final CropRepository cropRepository;
    private final MeasurementRepository measurementRepository;
    private final ControlActionRepository controlActionRepository;

    public GrowRoomQueryServiceImpl(GrowRoomRepository growRoomRepository, CropRepository cropRepository, MeasurementRepository measurementRepository, ControlActionRepository controlActionRepository) {
        this.growRoomRepository = growRoomRepository;
        this.cropRepository = cropRepository;
        this.measurementRepository = measurementRepository;
        this.controlActionRepository = controlActionRepository;
    }

    @Override
    public Optional<GrowRoom> handle(GetGrowRoomByIdQuery query) {
        return this.growRoomRepository.findById(query.id()).map(this::populateGrowRoomDetails);
    }

    @Override
    public List<GrowRoom> handle(GetGrowRoomsByCompanyIdQuery query) {
        List<GrowRoom> growRooms = growRoomRepository.findAllByCompanyId(query.companyId());
        return growRooms.stream()
                .map(this::populateGrowRoomDetails)
                .collect(Collectors.toList());
    }

    private GrowRoom populateGrowRoomDetails(GrowRoom growRoom) {
        cropRepository.findFirstByGrowRoomIdAndEndDateIsNull(growRoom.getId()).ifPresent(activeCrop -> {
            if (activeCrop.getCurrentPhase() != null) {
                Long currentPhaseId = activeCrop.getCurrentPhase().getId();
                List<ControlAction> latestActions = controlActionRepository.findLatestControlActionsByCropPhaseId(currentPhaseId);
                Map<ActuatorType, ControlActionType> states = latestActions.stream()
                        .collect(Collectors.toMap(
                                ControlAction::getActuatorType,
                                ControlAction::getControlActionType,
                                (existing, replacement) -> replacement
                        ));
                growRoom.setActuatorStates(states);

                measurementRepository.findLatestMeasurementTimestamp(currentPhaseId).ifPresent(lastTimestamp -> {
                    List<Measurement> lastMeasurements = measurementRepository.findAllByCropPhaseIdAndTimestamp(currentPhaseId, lastTimestamp);
                    growRoom.setLatestMeasurements(lastMeasurements);
                });
            }
        });
        return growRoom;
    }
}
