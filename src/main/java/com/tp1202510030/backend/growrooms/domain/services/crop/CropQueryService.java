package com.tp1202510030.backend.growrooms.domain.services.crop;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetCropByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetCropsByGrowRoomIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetFinishedCropsQuery;

import java.util.List;
import java.util.Optional;

public interface CropQueryService {
    Optional<Crop> handle(GetCropByIdQuery query);
    List<Crop> handle(GetCropsByGrowRoomIdQuery query);
    List<Crop> handle(GetFinishedCropsQuery query);
}
