package com.tp1202510030.backend.growrooms.domain.model.events;

import com.tp1202510030.backend.growrooms.domain.model.aggregates.Crop;
import com.tp1202510030.backend.growrooms.domain.model.entities.CropPhase;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ThresholdsUpdatedEvent extends ApplicationEvent {
    private final Crop crop;
    private final CropPhase phase; // Puede ser null si el cultivo ha terminado

    public ThresholdsUpdatedEvent(Object source, Crop crop, CropPhase phase) {
        super(source);
        this.crop = crop;
        this.phase = phase;
    }

}