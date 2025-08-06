package com.tp1202510030.backend.growrooms.domain.services.crop;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetCropByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetCropsByGrowRoomIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.crop.GetFinishedCropsByGrowRoomIdQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CropQueryService {
    Optional<Crop> handle(GetCropByIdQuery query);

    Page<Crop> handle(GetCropsByGrowRoomIdQuery query, Pageable pageable);

    Page<Crop> handle(GetFinishedCropsByGrowRoomIdQuery query, Pageable pageable);
}
