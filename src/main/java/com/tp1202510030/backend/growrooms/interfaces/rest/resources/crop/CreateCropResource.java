package com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop;

import com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase.CreateCropPhaseResource;

import java.time.Duration;
import java.util.List;
import java.util.Date;

public record CreateCropResource(
        Date startDate,
        Date endDate,
        Duration sensorActivationFrequency,
        Long growRoomId,
        List<CreateCropPhaseResource> phases
) {}