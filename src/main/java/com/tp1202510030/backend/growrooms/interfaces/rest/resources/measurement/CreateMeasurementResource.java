package com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Create Crop Resource",
        example = """
                {
                  "measurements": [
                    {
                      "parameter": "AIR_TEMPERATURE",
                      "value": 1.1
                    },
                    {
                      "parameter": "AIR_HUMIDITY",
                      "value": 1.1
                    },
                    {
                      "parameter": "CARBON_DIOXIDE",
                      "value": 1.1
                    },
                    {
                      "parameter": "SOIL_TEMPERATURE",
                      "value": 1.1
                    },
                    {
                      "parameter": "SOIL_MOISTURE",
                      "value": 1.1
                    }
                  ]
                }
                """
)
public record CreateMeasurementResource(
        Parameters parameter,
        Double value
) {
}
