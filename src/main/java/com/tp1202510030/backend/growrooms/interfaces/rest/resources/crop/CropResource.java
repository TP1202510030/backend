package com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop;

import com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase.CropPhaseResource;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public record CropResource(
        Long id,
        Date startDate,
        Date endDate,
        Duration sensorActivationFrequency,
        Long growRoomId,
        List<CropPhaseResource> phases,
        CropPhaseResource currentPhase
) {
}
