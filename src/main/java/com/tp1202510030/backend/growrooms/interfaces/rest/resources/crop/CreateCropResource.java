package com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop;

import com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase.CreateCropPhaseResource;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Schema(
        description = "Create Crop Resource",
        example = """
                    {
                      "startDate": "2025-06-03T19:59:39.612Z",
                      "endDate": "",
                      "sensorActivationFrequency": "PT5H30M15S",
                      "growRoomId": 1,
                      "phases": [
                        {
                          "name": "Incubation",
                          "phaseDuration": "PT720H",
                          "parameterThresholds": {
                            "airTemperatureMin": 18.0,
                            "airTemperatureMax": 25.0,
                            "airHumidityMin": 40.0,
                            "airHumidityMax": 60.0,
                            "carbonDioxideMin": 300.0,
                            "carbonDioxideMax": 800.0,
                            "soilTemperatureMin": 20.0,
                            "soilTemperatureMax": 24.0,
                            "soilMoistureMin": 30.0,
                            "soilMoistureMax": 50.0
                          }
                        },
                        {
                          "name": "Casing",
                          "phaseDuration": "PT1440H",
                          "parameterThresholds": {
                            "airTemperatureMin": 20.0,
                            "airTemperatureMax": 28.0,
                            "airHumidityMin": 45.0,
                            "airHumidityMax": 65.0,
                            "carbonDioxideMin": 350.0,
                            "carbonDioxideMax": 900.0,
                            "soilTemperatureMin": 21.0,
                            "soilTemperatureMax": 26.0,
                            "soilMoistureMin": 35.0,
                            "soilMoistureMax": 55.0
                          }
                        },
                        {
                          "name": "Induction",
                          "phaseDuration": "PT720H",
                          "parameterThresholds": {
                            "airTemperatureMin": 22.0,
                            "airTemperatureMax": 27.0,
                            "airHumidityMin": 50.0,
                            "airHumidityMax": 60.0,
                            "carbonDioxideMin": 400.0,
                            "carbonDioxideMax": 850.0,
                            "soilTemperatureMin": 22.0,
                            "soilTemperatureMax": 25.0,
                            "soilMoistureMin": 40.0,
                            "soilMoistureMax": 60.0
                          }
                        }
                      ]
                    }
                """
)
public record CreateCropResource(
        Date startDate,
        Date endDate,
        Duration sensorActivationFrequency,
        Long growRoomId,
        List<CreateCropPhaseResource> phases
) {
}