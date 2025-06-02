package com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.GrowRoomName;

public record UpdateGrowRoomResource(
        Long id,
        GrowRoomName name,
        String imageUrl,
        Long companyId
) {
}
