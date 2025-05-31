package com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom;

import com.tp1202510030.backend.growrooms.domain.model.commands.growroom.CreateGrowRoomCommand;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.CreateGrowRoomResource;

public class CreateGrowRoomCommandFromResourceAssembler {
    public static CreateGrowRoomCommand toCommandFromResource(CreateGrowRoomResource resource) {
        return new CreateGrowRoomCommand(
                resource.name(),
                resource.imageUrl(),
                resource.companyId()
        );
    }
}
