package com.tp1202510030.backend.growrooms.interfaces.rest.transform.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase.CropPhaseResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.MeasurementResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement.MeasurementResourceFromEntityAssembler;

import java.util.List;

public class CropPhaseResourceFromEntityAssembler {
    public static CropPhaseResource toResourceFromEntity(CropPhase entity) {
        List<MeasurementResource> measurementResources = entity.getMeasurements().stream()
                .map(MeasurementResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return new CropPhaseResource(
                entity.getId(),
                entity.getName().name(),
                entity.getDuration(),
                entity.getThresholds(),
                measurementResources
        );
    }
}
