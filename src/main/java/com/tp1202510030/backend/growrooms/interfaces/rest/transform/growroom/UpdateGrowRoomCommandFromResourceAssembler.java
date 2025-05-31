package com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom;

import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.UpdateGrowRoomCommand;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.UpdateGrowRoomResource;

public class UpdateGrowRoomCommandFromResourceAssembler {
    public static UpdateGrowRoomCommand toCommandFromResource(Long growRoomId, UpdateGrowRoomResource resource) {
        return new UpdateGrowRoomCommand(
                growRoomId,
                resource.name(),
                resource.imageUrl(),
                resource.companyId()
        );
    }
}
