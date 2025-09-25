package com.tp1202510030.backend.growrooms.domain.services.growroom;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.*;
import com.tp1202510030.backend.growrooms.domain.model.valueobjects.DeviceCredentials;

import java.util.Optional;

public interface GrowRoomCommandService {
    Optional<DeviceCredentials> handle(CreateGrowRoomCommand command);

    Optional<GrowRoom> handle(UpdateGrowRoomCommand command);

    Optional<GrowRoom> handle(PatchGrowRoomCommand command);

    void handle(DeactivateGrowRoomCropCommand command);

    void handle(ActivateGrowRoomCropCommand command);

    void handle(DeleteGrowRoomCommand command);

    void handle(DeleteAllGrowRoomsByCompanyIdCommand command);
}
