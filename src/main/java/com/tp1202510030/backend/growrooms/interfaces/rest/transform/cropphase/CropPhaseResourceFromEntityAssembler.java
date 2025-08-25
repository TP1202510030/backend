package com.tp1202510030.backend.growrooms.interfaces.rest.transform.cropphase;

import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.cropphase.CropPhaseResource;
import com.tp1202510030.backend.shared.interfaces.rest.transform.DurationFormatter;

public class CropPhaseResourceFromEntityAssembler {
    public static CropPhaseResource toResourceFromEntity(CropPhase entity) {

        return new CropPhaseResource(
                entity.getId(),
                entity.getName().name(),
                DurationFormatter.format(entity.getDuration()),
                entity.getThresholds()
        );
    }
}
