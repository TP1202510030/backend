package com.tp1202510030.backend.growrooms.application.internal.queryservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomsByCompanyIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.GrowRoomRepository;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrowRoomQueryServiceImpl implements GrowRoomQueryService {
    private final GrowRoomRepository growRoomRepository;
    private final CropRepository cropRepository;
    private final MeasurementRepository measurementRepository;

    public GrowRoomQueryServiceImpl(GrowRoomRepository growRoomRepository, CropRepository cropRepository, MeasurementRepository measurementRepository) {
        this.growRoomRepository = growRoomRepository;
        this.cropRepository = cropRepository;
        this.measurementRepository = measurementRepository;
    }

    @Override
    public Optional<GrowRoom> handle(GetGrowRoomByIdQuery query) {
        return this.growRoomRepository.findById(query.id());
    }

    @Override
    public List<GrowRoom> handle(GetGrowRoomsByCompanyIdQuery query) {
        List<GrowRoom> growRooms = growRoomRepository.findAllByCompanyId(query.companyId());

        growRooms.forEach(growRoom -> {
            Optional<Crop> activeCrop = cropRepository.findFirstByGrowRoomIdAndEndDateIsNull(growRoom.getId());
            if (activeCrop.isPresent()) {
                Crop crop = activeCrop.get();
                if (crop.getCurrentPhase() != null) {
                    Measurement lastMeasurement = measurementRepository.findTopByCropPhaseIdOrderByTimestampDesc(crop.getCurrentPhase().getId());
                    if (lastMeasurement != null) {
                        growRoom.setLatestMeasurement(lastMeasurement);
                    }
                } else {
                    growRoom.setLatestMeasurement(null);
                }
            }
        });

        return growRooms;
    }
}
