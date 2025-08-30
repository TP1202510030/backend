package com.tp1202510030.backend.growrooms.domain.services.growroom;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomsByCompanyIdQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GrowRoomQueryService {
    Optional<GrowRoom> handle(GetGrowRoomByIdQuery query);

    Page<GrowRoom> handle(GetGrowRoomsByCompanyIdQuery query, Pageable pageable);
}
