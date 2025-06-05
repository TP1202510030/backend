package com.tp1202510030.backend.growrooms.interfaces.rest.transform.measurement;

import com.tp1202510030.backend.growrooms.domain.model.entities.Measurement;
import com.tp1202510030.backend.growrooms.interfaces.rest.resources.measurement.MeasurementResource;

public class MeasurementResourceFromEntityAssembler {

    public static MeasurementResource toResourceFromEntity(Measurement entity) {
        return new MeasurementResource(
                entity.getId(),
                entity.getParameter(),
                entity.getValue()
        );
    }
}
