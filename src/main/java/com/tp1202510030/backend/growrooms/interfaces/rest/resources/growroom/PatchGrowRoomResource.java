package com.tp1202510030.backend.growrooms.interfaces.rest.resources.growroom;

import java.util.Optional;

public record PatchGrowRoomResource(
        Optional<String> name,
        Optional<String> imageUrl
) {
}
