package com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop;

public record FinishCropResource(Double totalProduction) {
    public FinishCropResource {
        if (totalProduction == null || totalProduction < 0) {
            throw new IllegalArgumentException("Total production cannot be null or negative.");
        }
    }
}
