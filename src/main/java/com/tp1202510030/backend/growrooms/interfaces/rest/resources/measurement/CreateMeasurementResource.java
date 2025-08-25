package com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement;

import com.tp1202510030.backend.growrooms.domain.model.valueobjects.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Represents a single environmental measurement to be created.",
        example = """
                {
                  "parameter": "AIR_TEMPERATURE",
                  "value": 22.5
                }
                """
)
public record CreateMeasurementResource(
        Parameters parameter,
        Double value
) {
}
