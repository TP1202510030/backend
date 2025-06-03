package com.tp1202510030.backend.growrooms.application.internal.queryservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetCropByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetCropsByGrowRoomIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetFinishedCropsQuery;
import com.tp1202510030.backend.growrooms.domain.services.crop.CropQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.CropRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CropQueryServiceImpl implements CropQueryService {
    private final CropRepository cropRepository;
    public CropQueryServiceImpl(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    @Override
    public Optional<Crop> handle(GetCropByIdQuery query) {
        return this.cropRepository.findById(query.id());
    }

    @Override
    public List<Crop> handle(GetCropsByGrowRoomIdQuery query) {
        return this.cropRepository.findAllByGrowRoomId(query.growRoomId());
    }

    @Override
    public List<Crop> handle(GetFinishedCropsQuery query) {
        return this.cropRepository.findByEndDateIsNotNull();
    }
}
