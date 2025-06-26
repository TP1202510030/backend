package com.tp1202510030.backend.growrooms.domain.model.commands.crop;

public record FinishCropCommand(Long cropId, Double totalProduction) {

    public FinishCropCommand {
        if (cropId == null) {
            throw new IllegalArgumentException("Crop ID cannot be null.");
        }
        if (totalProduction == null || totalProduction < 0) {
            throw new IllegalArgumentException("Total production must be a non-negative number.");
        }
    }
}
