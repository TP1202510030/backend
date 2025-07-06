package com.tp1202510030.backend.growrooms.domain.model.commands.crop;

public record AdvanceCropPhaseCommand(Long cropId) {
    public AdvanceCropPhaseCommand {
        if (cropId == null) {
            throw new IllegalArgumentException("Crop ID cannot be null");
        }
    }
}
