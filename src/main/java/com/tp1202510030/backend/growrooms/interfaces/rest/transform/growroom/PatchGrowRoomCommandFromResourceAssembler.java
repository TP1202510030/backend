package com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom;

import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.PatchGrowRoomCommand;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.PatchGrowRoomResource;

public class PatchGrowRoomCommandFromResourceAssembler {
    public static PatchGrowRoomCommand toCommandFromResource(Long growRoomId, PatchGrowRoomResource resource) {
        return new PatchGrowRoomCommand(
                growRoomId,
                resource.name(),
                resource.imageUrl()
        );
    }
}
