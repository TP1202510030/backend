package com.tp1202510030.backend.growrooms.domain.model.commands.growroom;

import java.util.Optional;

public record PatchGrowRoomCommand(
        Long growRoomId,
        Optional<String> name,
        Optional<String> imageUrl
) {
}