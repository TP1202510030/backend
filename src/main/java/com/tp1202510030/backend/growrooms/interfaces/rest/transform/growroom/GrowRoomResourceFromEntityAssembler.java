package com.tp1202510030.backend.growrooms.interfaces.rest.transform.growroom;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.GrowRoom;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom.GrowRoomResource;

public class GrowRoomResourceFromEntityAssembler {
    public static GrowRoomResource toResourceFromEntity(GrowRoom entity){
        return new GrowRoomResource(
                entity.getId(),
                entity.getGrowRoomName(),
                entity.getImageUrl(),
                entity.getCompany()
        );
    }
}
