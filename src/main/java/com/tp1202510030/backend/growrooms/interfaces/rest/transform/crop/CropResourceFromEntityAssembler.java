package com.tp1202510030.backend.growrooms.interfaces.rest.transform.crop;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.crop.CropResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase.CropPhaseResource;
import com.tp1202510030.backend.growrooms.interfaces.rest.transform.cropphase.CropPhaseResourceFromEntityAssembler;
import com.tp1202510030.backend.shared.interfaces.rest.transform.DateFormatter;
import com.tp1202510030.backend.shared.interfaces.rest.transform.DurationFormatter;

import java.util.List;

public class CropResourceFromEntityAssembler {
    public static CropResource toResourceFromEntity(Crop entity) {
        List<CropPhaseResource> phaseResources = entity.getPhases().stream()
                .map(CropPhaseResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        CropPhaseResource currentPhaseResource = null;
        if (entity.getCurrentPhase() != null) {
            currentPhaseResource = CropPhaseResourceFromEntityAssembler.toResourceFromEntity(entity.getCurrentPhase());
        }

        return new CropResource(
                entity.getId(),
                DateFormatter.format(entity.getStartDate()),
                DateFormatter.format(entity.getEndDate()),
                DurationFormatter.format(entity.getSensorActivationFrequency()),
                entity.getGrowRoom().getId(),
                phaseResources,
                currentPhaseResource,
                entity.getTotalProduction()
        );
    }
}
