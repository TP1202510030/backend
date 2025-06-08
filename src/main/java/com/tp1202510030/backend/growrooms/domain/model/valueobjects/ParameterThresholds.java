package com.tp1202510030.backend.growrooms.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ParameterThresholds {
    private Double airTemperatureMin;
    private Double airTemperatureMax;

    private Double airHumidityMin;
    private Double airHumidityMax;

    private Double carbonDioxideMin;
    private Double carbonDioxideMax;

    private Double soilTemperatureMin;
    private Double soilTemperatureMax;

    private Double soilMoistureMin;
    private Double soilMoistureMax;

    public ParameterThresholds() {
    }

    public ParameterThresholds(Double airTemperatureMin, Double airTemperatureMax,
                               Double airHumidityMin, Double airHumidityMax,
                               Double carbonDioxideMin, Double carbonDioxideMax,
                               Double soilTemperatureMin, Double soilTemperatureMax,
                               Double soilMoistureMin, Double soilMoistureMax) {
        this.airTemperatureMin = airTemperatureMin;
        this.airTemperatureMax = airTemperatureMax;
        this.airHumidityMin = airHumidityMin;
        this.airHumidityMax = airHumidityMax;
        this.carbonDioxideMin = carbonDioxideMin;
        this.carbonDioxideMax = carbonDioxideMax;
        this.soilTemperatureMin = soilTemperatureMin;
        this.soilTemperatureMax = soilTemperatureMax;
        this.soilMoistureMin = soilMoistureMin;
        this.soilMoistureMax = soilMoistureMax;
    }

    public double getMinimum(Parameters parameter) {
        return switch (parameter) {
            case AIR_TEMPERATURE -> airTemperatureMin;
            case AIR_HUMIDITY -> airHumidityMin;
            case CARBON_DIOXIDE -> carbonDioxideMin;
            case SOIL_TEMPERATURE -> soilTemperatureMin;
            case SOIL_MOISTURE -> soilMoistureMin;
            default -> throw new IllegalArgumentException("Unknown parameter " + parameter);
        };
    }

    public double getMaximum(Parameters parameter) {
        return switch (parameter) {
            case AIR_TEMPERATURE -> airTemperatureMax;
            case AIR_HUMIDITY -> airHumidityMax;
            case CARBON_DIOXIDE -> carbonDioxideMax;
            case SOIL_TEMPERATURE -> soilTemperatureMax;
            case SOIL_MOISTURE -> soilMoistureMax;
            default -> throw new IllegalArgumentException("Unknown parameter" + parameter);
        };
    }
}

