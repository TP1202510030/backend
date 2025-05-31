package com.tp1202510030.backend.growrooms.domain.services.growroom;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.CreateGrowRoomCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.UpdateGrowRoomCommand;

import java.util.Optional;

public interface GrowRoomCommandService {
    Long handle(CreateGrowRoomCommand command);
    Optional<GrowRoom> handle(UpdateGrowRoomCommand command);
}
