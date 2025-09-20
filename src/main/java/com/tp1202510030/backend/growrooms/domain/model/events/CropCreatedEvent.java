package com.tp1202510030.backend.growrooms.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CropCreatedEvent extends ApplicationEvent {
    private final Long growRoomId;

    public CropCreatedEvent(Object source, Long growRoomId) {
        super(source);
        this.growRoomId = growRoomId;
    }
}