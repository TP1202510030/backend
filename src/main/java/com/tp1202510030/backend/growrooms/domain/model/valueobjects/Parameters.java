package com.tp1202510030.backend.growrooms.domain.model.valueobjects;

public enum Parameters {
    AIR_TEMPERATURE(UnitOfMeasurements.CELSIUS),
    AIR_HUMIDITY(UnitOfMeasurements.PERCENTAGE),
    CARBON_DIOXIDE(UnitOfMeasurements.PARTS_PER_MILLION),
    SOIL_TEMPERATURE(UnitOfMeasurements.CELSIUS),
    SOIL_MOISTURE(UnitOfMeasurements.PERCENTAGE);

    private final UnitOfMeasurements unit;

    Parameters(UnitOfMeasurements unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit.getDisplayName();
    }
}