package com.tp1202510030.backend.growrooms.application.internal.queryservices;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomByIdQuery;
import com.tp1202510030.backend.growrooms.domain.model.queries.growroom.GetGrowRoomsByCompanyIdQuery;
import com.tp1202510030.backend.growrooms.domain.services.growroom.GrowRoomQueryService;
import com.tp1202510030.backend.growrooms.infrastructure.persistence.jpa.repositories.GrowRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrowRoomQueryServiceImpl implements GrowRoomQueryService {
    private final GrowRoomRepository growRoomRepository;

    public GrowRoomQueryServiceImpl(GrowRoomRepository growRoomRepository) {
        this.growRoomRepository = growRoomRepository;
    }

    @Override
    public Optional<GrowRoom> handle(GetGrowRoomByIdQuery query) {
        return this.growRoomRepository.findById(query.id());
    }

    @Override
    public List<GrowRoom> handle(GetGrowRoomsByCompanyIdQuery query) {
        return growRoomRepository.findAllByCompanyId(query.companyId());
    }
}
