package com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Request body to add a list of new measurements.",
        example = """
                {
                  "measurements": [
                    {
                      "parameter": "AIR_TEMPERATURE",
                      "value": 22.5
                    },
                    {
                      "parameter": "AIR_HUMIDITY",
                      "value": 85.0
                    },
                    {
                      "parameter": "CARBON_DIOXIDE",
                      "value": 750.0
                    },
                    {
                      "parameter": "SOIL_TEMPERATURE",
                      "value": 24.0
                    },
                    {
                      "parameter": "SOIL_MOISTURE",
                      "value": 60.5
                    }
                  ]
                }
                """
)
public record AddMeasurementsToCurrentPhaseResource(
        List<CreateMeasurementResource> measurements
) {
}
