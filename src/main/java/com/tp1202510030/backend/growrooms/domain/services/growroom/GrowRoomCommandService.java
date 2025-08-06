package com.tp1202510030.backend.growrooms.domain.services.growroom;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.ActivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.CreateGrowRoomCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.DeactivateGrowRoomCropCommand;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.UpdateGrowRoomCommand;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.DeviceCredentials;

import java.util.Optional;

public interface GrowRoomCommandService {
    Optional<DeviceCredentials> handle(CreateGrowRoomCommand command);

    Optional<GrowRoom> handle(UpdateGrowRoomCommand command);

    void handle(DeactivateGrowRoomCropCommand command);

    void handle(ActivateGrowRoomCropCommand command);
}
