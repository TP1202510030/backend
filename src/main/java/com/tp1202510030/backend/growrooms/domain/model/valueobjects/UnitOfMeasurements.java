package com.tp1202510030.backend.growrooms.domain.model.valueobjects;

public enum UnitOfMeasurements {
    CELSIUS("ºC"),
    PERCENTAGE("%"),
    PARTS_PER_MILLION("ppm");

    private final String displayName;

    UnitOfMeasurements(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
