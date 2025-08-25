package com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop;

import com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase.CropPhaseResource;

import java.util.List;

public record CropResource(
        Long id,
        String startDate,
        String endDate,
        String sensorActivationFrequency,
        Long growRoomId,
        List<CropPhaseResource> phases,
        CropPhaseResource currentPhase,
        Double totalProduction
) {
}
