package com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom;

public record UpdateGrowRoomResource(
        Long id,
        String name,
        String imageUrl,
        Long companyId
) {
}
